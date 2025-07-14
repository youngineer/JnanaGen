package com.youngineer.backend.services.implementations;

import com.youngineer.backend.services.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    private static final String apiKey = System.getenv("HUGGINGFACE_API_KEY");
    private static final String endpointUrl = System.getenv("HUGGINGFACE_ENDPOINT_URL");
    private final OpenAiChatModel chatModel;
    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

    public ChatServiceImpl(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public ChatResponse getAiResponse(Prompt prompt) {
        logger.info("Generating the ai response: " + prompt.toString());
        try {
            return chatModel.call(
                    prompt
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
