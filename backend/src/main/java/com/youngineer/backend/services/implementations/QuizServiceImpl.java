package com.youngineer.backend.services.implementations;

import com.youngineer.backend.dto.requests.QuizRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import com.youngineer.backend.models.Option;
import com.youngineer.backend.models.Question;
import com.youngineer.backend.models.Quiz;
import com.youngineer.backend.models.User;
import com.youngineer.backend.repository.OptionRepository;
import com.youngineer.backend.repository.QuizRepository;
import com.youngineer.backend.repository.UserRepository;
import com.youngineer.backend.services.ChatService;
import com.youngineer.backend.services.QuizService;
import com.youngineer.backend.utils.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.List;


@Service
@Transactional
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final OptionRepository optionRepository;
    private final ChatService chatService;
    private final UserRepository userRepository;
    @Value("${OPENROUTER_API_KEY}")
    private String apiKey;
    private String apiEndpoint = "https://openrouter.ai/api/v1/completions";
    private final String model = "deepseek/deepseek-chat-v3-0324:free";
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);

    public QuizServiceImpl(ChatService chatService, Constants constants, QuizRepository quizRepository, OptionRepository optionRepository, UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.optionRepository = optionRepository;
        this.chatService = chatService;
        this.userRepository = userRepository;
    }


    public ResponseDto generateQuiz(QuizRequest generateQuizRequest) {
        String prompt  = Constants.BASE_PROMPT;
        prompt += "\nUser notes: " + generateQuizRequest.userNotes();
        prompt += "\nAdditional notes: " + generateQuizRequest.additionalContext();
        prompt += "\nTotal Questions: " + generateQuizRequest.totalQuestions();
        prompt += "\nNumber of Options per question: " + generateQuizRequest.numberOfOptions();

        try {
            JSONObject response = getQuizFromAi(prompt);
            Quiz quiz = storeToDb(response, 1L);
            return new ResponseDto("OK", quiz);
        } catch (Exception e) {
            return new ResponseDto("Error creating user: " + e, null);
        }
    }


    @Override
    public ResponseDto generateScore(List<String> answers) {
        return null;
    }


    private JSONObject getQuizFromAi(String prompt) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            JSONObject jsonPayload = new JSONObject();
            jsonPayload.put("model", model);
            jsonPayload.put("prompt", prompt);

            HttpEntity<String> request = new HttpEntity<>(jsonPayload.toString(), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(apiEndpoint, request, String.class);

            JSONObject jsonResponse = new JSONObject(response.getBody());
            JSONArray choices = jsonResponse.getJSONArray("choices");
            String text = choices.getJSONObject(0).getString("text").trim();
            String quizText = extractFirstJsonObject(text);
            JSONObject quizJson = new JSONObject(quizText);


            return quizJson;
        } catch (HttpClientErrorException e) {
            logger.error("API error for prompt: {}", prompt, e);
            throw new RuntimeException("Failed to fetch quiz from AI service", e);
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


    private static String extractFirstJsonObject(String text) {
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        throw new JSONException("No valid JSON object found in text");
    }

}
