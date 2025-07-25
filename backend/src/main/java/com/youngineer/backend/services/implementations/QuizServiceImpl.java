package com.youngineer.backend.services.implementations;

import com.youngineer.backend.dto.requests.QuizInfo;
import com.youngineer.backend.dto.requests.QuizRequest;
import com.youngineer.backend.dto.requests.QuizResultRequest;
import com.youngineer.backend.dto.responses.*;
import com.youngineer.backend.models.*;
import com.youngineer.backend.repository.*;
import com.youngineer.backend.services.QuizService;
import com.youngineer.backend.utils.Constants;
import com.youngineer.backend.utils.ResponseUtil;
import jakarta.persistence.EntityNotFoundException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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


    @Override
    @Transactional
    public ResponseEntity<ResponseDto> generateQuiz(String emailId, QuizRequest generateQuizRequest) {
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
            Long quizId = storeToDb(quizResponse, user.getId());

            return ResponseUtil.success("Quiz generated successfully", quizId);

        } catch (EntityNotFoundException e) {
            logger.error("Entity not found: {}", e.getMessage());
            return ResponseUtil.notFound(e.getMessage());
        } catch (JSONException e) {
            logger.error("JSON parsing error: {}", e.getMessage());
            return ResponseUtil.internalServerError("Error parsing AI response: "+ e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return ResponseUtil.internalServerError("Error creating quiz: " + e.getMessage());
        }
    }


    public ResponseEntity<ResponseDto> loadQuiz(String emailId, Long quizId) {
        try {
            User user = userRepository.findByEmailId(emailId)
                    .orElseThrow(() -> new EntityNotFoundException("User with email: " + emailId + " does not exist"));

            Quiz quiz = quizRepository.findById(quizId)
                    .orElseThrow(() -> new EntityNotFoundException("Quiz with id: " +quizId + " does not exist"));

            if(user != quiz.getUser()) throw new IllegalCallerException("Unauthorized request");

            return ResponseUtil.success("Quiz retrieved successfully!", convertToQuizDto(quiz));


        } catch (EntityNotFoundException e) {
            logger.error("Entity not found: {}", e.getMessage());
            return ResponseUtil.notFound("Quiz or User not found");
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return ResponseUtil.internalServerError("Error calculating score: " + e.getMessage());
        }
    }

    public ResponseEntity<ResponseDto> isQuizResultGenerated(String emailId, Long quizId) {
        try {
            User user = userRepository.findByEmailId(emailId)
                    .orElseThrow(() -> new EntityNotFoundException("User with email: " + emailId + " does not exist"));

            Quiz quiz = quizRepository.findById(quizId)
                    .orElseThrow(() -> new EntityNotFoundException("Quiz with id: " +quizId + " does not exist"));

            if(user != quiz.getUser()) throw new IllegalCallerException("Unauthorized request");

            if(!quizResultRepository.existsQuizResultsByUserAndQuiz(user, quiz)) {
                return ResponseUtil.success("Quiz not attempted", "/quiz/" + quizId);
            } else {
                return ResponseUtil.success("Quiz attempted", "/quizInfo/" + quizId);
            }

        } catch (EntityNotFoundException e) {
            logger.error("Entity not found: {}", e.getMessage());
            return ResponseUtil.notFound("Quiz or User not found");
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return ResponseUtil.internalServerError("Error calculating score: " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseDto> calculateScore(String emailId, QuizResultRequest request) {
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

            return ResponseUtil.success("Score calculated successfully!", "/quizInfo/" + quizId);

        } catch (EntityNotFoundException e) {
            logger.error("Entity not found: {}", e.getMessage());
            return ResponseUtil.notFound("Quiz or User not found");
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return ResponseUtil.internalServerError("Error calculating score: " + e.getMessage());
        }
    }


    public ResponseEntity<ResponseDto> getQuizInfo(String emailId, QuizInfo request) {
        Long userId = request.userId();
        Long quizId = request.quizId();

        try {
            User user = userRepository.findByEmailId(emailId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

            Quiz quiz = quizRepository.findById(quizId)
                    .orElseThrow(() -> new EntityNotFoundException("Quiz not found with ID: " + quizId));


            return ResponseUtil.success("Quiz result retrieved successfully!", getResult(user, quiz).content());

        } catch (EntityNotFoundException e) {
            logger.error("Entity not found: {}", e.getMessage());
            return ResponseUtil.notFound("User or Quiz not found");
        } catch (IllegalCallerException e) {
            logger.warn("Unauthorized access: {}", e.getMessage());
            return ResponseUtil.badRequest("Unauthorized request");
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return ResponseUtil.internalServerError("Error fetching quiz info: " + e.getMessage());
        }
    }


    public ResponseEntity<ResponseDto> getUserDashboardData(String emailId) {
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

            return ResponseUtil.success("Dashboard data fetched successfully", content);

        } catch (EntityNotFoundException e) {
            logger.error("Entity not found: {}", e.getMessage());
            return ResponseUtil.notFound("User not found with email: " + emailId);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return ResponseUtil.internalServerError("Error fetching user dashboard data: " + e.getMessage());
        }
    }


    private Long storeToDb(JSONObject response, Long userId) {
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

//        return quizRepository.save(quiz);
            Quiz generatedQuiz = quizRepository.save(quiz);
        return generatedQuiz.getId();
    }

    private ResponseDto getResult(User user, Quiz quiz) {
        LinkedHashMap<Long, AnswerEvaluation> evaluationMap = new LinkedHashMap<>();
        try {
            List<QuizResult> quizResultList = quizResultRepository.findAllByUserAndQuiz(user, quiz);
            String title = quiz.getTitle();
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
            double percentage = (double) score / totalQuestions * 100;
            double finalPercentage = Math.round(percentage * 100.0) / 100.0;


            HashMap<String, Object> resultContent = new HashMap<>();
            resultContent.put("percentage", finalPercentage);
            resultContent.put("title", title);
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


/*
@Override
    @Transactional
    public ResponseEntity<ResponseDto> calculateScore(String emailId, QuizResultRequest request) {
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

            return ResponseUtil.success("Score calculated successfully!", getResult(user, quiz).content());

        } catch (EntityNotFoundException e) {
            logger.error("Entity not found: {}", e.getMessage());
            return ResponseUtil.notFound("Quiz or User not found");
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return ResponseUtil.internalServerError("Error calculating score: " + e.getMessage());
        }
    }
 */
