package com.youngineer.backend.services.implementations;

import com.youngineer.backend.dto.requests.QuizInfo;
import com.youngineer.backend.dto.requests.QuizRequest;
import com.youngineer.backend.dto.requests.QuizResultRequest;
import com.youngineer.backend.dto.responses.*;
import com.youngineer.backend.models.*;
import com.youngineer.backend.repository.*;
import com.youngineer.backend.services.QuizService;
import com.youngineer.backend.utils.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final OptionRepository optionRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuizResultRepository quizResultRepository;

    private final AIServiceImpl aiService;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);

    public QuizServiceImpl(QuizResultRepository quizResultRepository, AIServiceImpl aiService, QuizRepository quizRepository, OptionRepository optionRepository, UserRepository userRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.optionRepository = optionRepository;
        this.userRepository = userRepository;
        this.aiService = aiService;
        this.questionRepository = questionRepository;
        this.quizResultRepository = quizResultRepository;
    }


    public ResponseDto generateQuiz(String emailId, QuizRequest generateQuizRequest) {
        String prompt = Constants.BASE_PROMPT;
        prompt += "\nUser notes: " + generateQuizRequest.userNotes();
        prompt += "\nAdditional notes: " + generateQuizRequest.additionalContext();
        prompt += "\nTotal Questions: " + generateQuizRequest.totalQuestions();
        prompt += "\nNumber of Options per question: " + generateQuizRequest.numberOfOptions();

        try {
            User user = userRepository.findByEmailId(emailId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + emailId));

            String aiResponse = this.aiService.getAiResponse(prompt);
            JSONObject quizResponse = extractFirstJsonObject(aiResponse);
            Quiz quiz = storeToDb(quizResponse, user.getId());
            QuizResponse quizDto = convertToQuizDto(quiz);
            return new ResponseDto("OK", quizDto);
        } catch (Exception e) {
            return new ResponseDto("Error creating user: " + e, null);
        }
    }


    private Quiz storeToDb(JSONObject response, Long userId) {
        if (!response.has("title") || !response.has("quiz")) {
            logger.error("Invalid JSON response: missing title or quiz array");
            throw new IllegalArgumentException("Invalid quiz response format");
        }

        String title = response.getString("title");
        JSONArray quizArray = response.getJSONArray("quiz");
        if (quizArray.isEmpty()) {
            logger.warn("Quiz array is empty");
            throw new IllegalArgumentException("No questions provided in quiz");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setUpdatedAt(currentTime);
        quiz.setCreatedAt(currentTime);
        quiz.setUser(user);

        for (int i = 0; i < quizArray.length(); i++) {
            JSONObject questionObj = quizArray.getJSONObject(i);
            if (!questionObj.has("question") || !questionObj.has("options") || !questionObj.has("correct_answer")) {
                logger.error("Invalid question format at index {}", i);
                throw new IllegalArgumentException("Invalid question format");
            }

            Question question = new Question();
            question.setQuestionText(questionObj.getString("question"));
            question.setQuiz(quiz);
            question.setExplanation(questionObj.getString("explanation"));

            JSONArray options = questionObj.getJSONArray("options");
            if (options.isEmpty()) {
                logger.warn("No options provided for question at index {}", i);
                throw new IllegalArgumentException("Question must have at least one option");
            }

            for (int j = 0; j < options.length(); j++) {
                Option option = new Option();
                option.setOptionText(options.getString(j));
                option.setQuestion(question);
                question.getOptions().add(option);
            }

            question.setCorrectAns(questionObj.getString("correct_answer"));
            quiz.getQuestions().add(question);
        }

        return quizRepository.save(quiz);
    }


    @Override
    @Transactional
    public ResponseDto calculateScore(String emailId, QuizResultRequest request) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Long quizId = request.quizId();
        LinkedHashMap<Long, Long> userAnswersMap = request.questionOptionMap();

        try {
            Quiz quiz = quizRepository.findById(quizId)
                    .orElseThrow(() -> new EntityNotFoundException("Quiz not found with ID: " + quizId));

            User user = userRepository.findByEmailId(emailId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + emailId));

            for (Map.Entry<Long, Long> entry : userAnswersMap.entrySet()) {
                Long questionId = entry.getKey();
                Long selectedOptionId = entry.getValue();

                Question question = questionRepository.findById(questionId)
                        .orElseThrow(() -> new EntityNotFoundException("Question not found with ID: " + questionId));

                Option selectedOption = optionRepository.findById(selectedOptionId)
                        .orElseThrow(() -> new EntityNotFoundException("Option not found with ID: " + selectedOptionId));

                String correctAnswerText = question.getCorrectAns();
                String userAnswerText = selectedOption.getOptionText();
                boolean isCorrect = userAnswerText.equals(correctAnswerText);

                QuizResult quizResult = new QuizResult();
                quizResult.setUser(user);
                quizResult.setQuiz(quiz);
                quizResult.setQuestion(question);
                quizResult.setSelectedOption(selectedOption);
                quizResult.setCorrect(isCorrect);
                quizResult.setCreatedAt(currentTime);
                quizResult.setUpdatedAt(currentTime);
                quizResultRepository.save(quizResult);
            }

            return getResult(user, quiz);

        } catch (Exception ex) {
            return new ResponseDto("Error calculating score: " + ex.getMessage(), null);
        }
    }


    public ResponseDto getQuizInfo(String emailId, QuizInfo request) {
        Long userId = request.userId();
        Long quizId = request.quizId();

        try {
            User user = userRepository.findByEmailId(emailId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

            Quiz quiz = quizRepository.findById(quizId)
                    .orElseThrow(() -> new EntityNotFoundException("Quiz not found with ID: " + quizId));

            return getResult(user, quiz);
        } catch (Exception ex) {
            return new ResponseDto(ex.getMessage(), null);
        }
    }


    public ResponseDto getUserDashboardData(String emailId) {
        LinkedHashMap<Long, UserQuizDetails> userQuizDetailsMap = new LinkedHashMap<>();
        try {
            User user = userRepository.findByEmailId(emailId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + emailId));

            List<Quiz> userQuizzes = quizRepository.findAllByUser(user, Sort.by(Sort.Direction.DESC, "updatedAt"));
            String name = user.getName();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            for (Quiz quiz : userQuizzes) {
                Long quizId = quiz.getId();
                String title = quiz.getTitle();

                Timestamp createdAt = quiz.getCreatedAt();
                Timestamp updatedAt = quiz.getUpdatedAt();

                // Format timestamps as "yyyy-MM-dd"
                String createdFormatted = formatter.format(new Date(createdAt.getTime()));
                String updatedFormatted = formatter.format(new Date(updatedAt.getTime()));

                userQuizDetailsMap.put(quizId, new UserQuizDetails(title, createdFormatted, updatedFormatted));
            }

            HashMap<String, Object> content = new HashMap<>();
            content.put("userName", name);
            content.put("quizList", userQuizDetailsMap);

            return new ResponseDto("OK", content);

        } catch (Exception ex) {
            return new ResponseDto("Error(EntityNotFoundException) occurred while fetching from db: " + ex.getMessage(), null);
        }
    }



    private ResponseDto getResult(User user, Quiz quiz) {
        LinkedHashMap<Long, AnswerEvaluation> evaluationMap = new LinkedHashMap<>();
        try {
            List<QuizResult> quizResultList = quizResultRepository.findAllByUserAndQuiz(user, quiz);
            int score = 0;
            int totalQuestions = quizResultList.size();
            for(QuizResult result: quizResultList) {
                boolean isCorrect = result.isCorrect();
                Option option = result.getSelectedOption();
                Question questionEntity = result.getQuestion();
                String correctAnswer = questionEntity.getCorrectAns();
                String question = questionEntity.getQuestionText();
                Long questionId = questionEntity.getId();
                String userAnswer = option.getOptionText();
                String explanation = questionEntity.getExplanation();

                evaluationMap.put(questionId, new AnswerEvaluation(question, userAnswer, correctAnswer, isCorrect, explanation));
                if(isCorrect) score++;

            }
            HashMap<String, Object> resultContent = new HashMap<>();
            resultContent.put("score", score);
            resultContent.put("totalQuestions", totalQuestions);
            resultContent.put("evaluation", evaluationMap);

            return new ResponseDto("OK", resultContent);
        } catch (Exception ex) {
            return new ResponseDto("Error(EntityNotFoundException) occurred while fetching quizResult from db: " + ex.getMessage(), null);
        }
    }

    private static JSONObject extractFirstJsonObject(String text) {
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start >= 0 && end > start) {
            text = text.substring(start, end + 1);
            return new JSONObject(text);
        }
        throw new JSONException("No valid JSON object found in text");
    }

    private QuizResponse convertToQuizDto(Quiz quiz) {
        Long quizId = quiz.getId();
        String title = quiz.getTitle();
        List<QuestionDto> questionDtoList = new ArrayList<>();

        for (Question question : quiz.getQuestions()) {
            Long questionId = question.getId();
            String questionText = question.getQuestionText();
            List<OptionDto> optionDtoList = new ArrayList<>();

            for (Option option : question.getOptions()) {
                OptionDto optionDto = new OptionDto(option.getId(), option.getOptionText());
                optionDtoList.add(optionDto);
            }

            questionDtoList.add(new QuestionDto(questionId, questionText, optionDtoList));
        }

        return new QuizResponse(quizId, title, questionDtoList);
    }

}

//    private JSONObject getQuizFromAi(String prompt) {
//        try {
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.set("Authorization", "Bearer " + apiKey);
//
//            JSONObject jsonPayload = new JSONObject();
//            jsonPayload.put("model", model);
//            jsonPayload.put("prompt", prompt);
//
//            HttpEntity<String> request = new HttpEntity<>(jsonPayload.toString(), headers);
//            ResponseEntity<String> response = restTemplate.postForEntity(apiEndpoint, request, String.class);
//
//            JSONObject jsonResponse = new JSONObject(response.getBody());
//            JSONArray choices = jsonResponse.getJSONArray("choices");
//            String text = choices.getJSONObject(0).getString("text").trim();
//            String quizText = extractFirstJsonObject(text);
//            JSONObject quizJson = new JSONObject(quizText);
//
//
//            return quizJson;
//        } catch (HttpClientErrorException e) {
//            logger.error("API error for prompt: {}", prompt, e);
//            throw new RuntimeException("Failed to fetch quiz from AI service", e);
//        }
//    }
//}
