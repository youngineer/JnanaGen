package com.youngineer.backend.services.implementations;

import com.youngineer.backend.services.AIService;
import org.json.JSONException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;


@Service
public class AIServiceImpl implements AIService {
    private final ChatClient chatClient;

    public AIServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public String getAiResponse(String prompt) {
        try {
            return chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();
        } catch (Exception e) {
            throw new JSONException("Sorry, an error occurred while processing your message: " + e.toString());
        }
    }
}
