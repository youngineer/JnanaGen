package com.youngineer.backend.services.implementations;

import com.youngineer.backend.dto.requests.QuizRequest;
import com.youngineer.backend.dto.requests.QuizResultRequest;
import com.youngineer.backend.dto.responses.*;
import com.youngineer.backend.models.*;
import com.youngineer.backend.repository.*;
import com.youngineer.backend.services.ChatService;
import com.youngineer.backend.services.QuizService;
import com.youngineer.backend.utils.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;


@Service
@Transactional
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final OptionRepository optionRepository;
    private final ChatService chatService;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuizResultRepository quizResultRepository;

    private final AIServiceImpl aiService;

    @Value("${OPENROUTER_API_KEY}")
    private String apiKey;
    private String apiEndpoint = "https://openrouter.ai/api/v1/completions";
    private final String model = "deepseek/deepseek-chat-v3-0324:free";
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);

    public QuizServiceImpl(ChatService chatService, QuizResultRepository quizResultRepository, Constants constants, AIServiceImpl aiService, QuizRepository quizRepository, OptionRepository optionRepository, UserRepository userRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.optionRepository = optionRepository;
        this.chatService = chatService;
        this.userRepository = userRepository;
        this.aiService = aiService;
        this.questionRepository = questionRepository;
        this.quizResultRepository = quizResultRepository;
    }


    public ResponseDto generateQuiz(QuizRequest generateQuizRequest) {
        String prompt = Constants.BASE_PROMPT;
        prompt += "\nUser notes: " + generateQuizRequest.userNotes();
        prompt += "\nAdditional notes: " + generateQuizRequest.additionalContext();
        prompt += "\nTotal Questions: " + generateQuizRequest.totalQuestions();
        prompt += "\nNumber of Options per question: " + generateQuizRequest.numberOfOptions();

        try {
            String aiResponse = this.aiService.getAiResponse(prompt);
            JSONObject quizResponse = extractFirstJsonObject(aiResponse);
            Quiz quiz = storeToDb(quizResponse, 1L);
            QuizResponse quizDto = convertToQuizDto(quiz);
            return new ResponseDto("OK", quizDto);
        } catch (Exception e) {
            return new ResponseDto("Error creating user: " + e, null);
        }
    }


    @Override
    @Transactional
    public ResponseDto calculateScore(QuizResultRequest request) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Long userId = 1L;
        Long quizId = request.quizId();
        LinkedHashMap<Long, Long> questionOptionMap = request.questionOptionMap();
        LinkedHashMap<Long, AnswerEvaluation> questionIsCorrect = new LinkedHashMap<>();
        System.out.println(request.toString());

        int score = 0;
        int totalQuestions = questionOptionMap.size();

        try {
            Quiz quiz = quizRepository.findById(quizId)
                    .orElseThrow(() -> new EntityNotFoundException("Quiz not found with ID: " + quizId));

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

            for (Map.Entry<Long, Long> entry : questionOptionMap.entrySet()) {
                Long questionId = entry.getKey();
                Long optionId = entry.getValue();

                Question question = questionRepository.findById(questionId)
                        .orElseThrow(() -> new EntityNotFoundException("Question not found with ID: " + questionId));

                Option option = optionRepository.findById(optionId)
                        .orElseThrow(() -> new EntityNotFoundException("Option not found with ID: " + optionId));

                String correctAnswer = question.getCorrectAns();
                String userAnswer = option.getOptionText();
                boolean isCorrect = userAnswer.equals(correctAnswer);

                if (isCorrect) score++;

                questionIsCorrect.put(questionId, new AnswerEvaluation(isCorrect, correctAnswer));

                QuizResult quizResult = new QuizResult();
                quizResult.setUser(user);
                quizResult.setQuiz(quiz);
                quizResult.setQuestion(question);
                quizResult.setSelectedOption(option);
                quizResult.setCorrect(isCorrect);
                quizResult.setCreatedAt(currentTime);
                quizResult.setUpdatedAt(currentTime);

                quizResultRepository.save(quizResult);
            }

            HashMap<String, Object> responseMap = new HashMap<>();
            responseMap.put("score", score);
            responseMap.put("totalQuestions", totalQuestions);
            responseMap.put("resultAnalysis", questionIsCorrect);

            return new ResponseDto("OK", responseMap);

        } catch (Exception e) {
            return new ResponseDto("Error calculating score: " + e.getMessage(), null);
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
