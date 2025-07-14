package com.youngineer.backend.services;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

public interface ChatService {
    public ChatResponse getAiResponse(Prompt prompt);
}
