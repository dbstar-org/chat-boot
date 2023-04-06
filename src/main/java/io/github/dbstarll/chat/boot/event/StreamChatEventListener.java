package io.github.dbstarll.chat.boot.event;

import io.github.dbstarll.chat.boot.model.response.SseEmitterFutureCallback;
import io.github.dbstarll.utils.net.api.ApiException;
import io.github.dbstarll.utils.openai.OpenAiAsyncClient;
import io.github.dbstarll.utils.openai.model.fragment.Message;
import io.github.dbstarll.utils.openai.model.request.ChatRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;

@Async
@Component
class StreamChatEventListener implements ApplicationListener<StreamChatEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamChatEventListener.class);
    private static final int DEFAULT_MAX_TOKENS = 512;

    private final OpenAiAsyncClient openAiAsyncClient;

    StreamChatEventListener(final OpenAiAsyncClient openAiAsyncClient) {
        this.openAiAsyncClient = openAiAsyncClient;
    }

    @Override
    public void onApplicationEvent(final StreamChatEvent event) {
        LOGGER.info("onApplicationEvent: {}", event.getQuestion());
        final ChatRequest request = new ChatRequest();
        request.setModel("gpt-3.5-turbo");
        request.setMaxTokens(DEFAULT_MAX_TOKENS);
        request.setMessages(Collections.singletonList(Message.user(event.getQuestion().getContent())));

        final SseEmitter emitter = event.getEmitter();
        try {
            openAiAsyncClient.chat(request, new SseEmitterFutureCallback<>(emitter));
        } catch (ApiException | IOException e) {
            LOGGER.error("call chat failed", e);
            emitter.completeWithError(e);
        }
    }
}
