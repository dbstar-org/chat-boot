package io.github.dbstarll.chat.boot.event;

import io.github.dbstarll.chat.boot.model.response.SseEmitterFutureCallback;
import io.github.dbstarll.utils.net.api.StreamFutureCallback;
import io.github.dbstarll.utils.openai.model.api.ChatCompletionChunk;
import io.github.dbstarll.utils.openai.model.fragment.ChatChunkChoice;
import io.github.dbstarll.utils.openai.model.fragment.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Async
@Component
class TestStreamChatEventListener implements ApplicationListener<StreamChatEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestStreamChatEventListener.class);
    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private static final long INTERVAL = Duration.ofSeconds(1).toMillis();

    @Override
    public void onApplicationEvent(final StreamChatEvent event) {
        LOGGER.info("onApplicationEvent: {}", event.getQuestion());

        final StreamFutureCallback<ChatCompletionChunk> callback = new SseEmitterFutureCallback<>(event.getEmitter());
        final String id = UUID.randomUUID().toString();

        final Instant start = new Date().toInstant();
        while (true) {
            final Duration duration = Duration.between(start, new Date().toInstant());
            if (duration.compareTo(TIMEOUT) > 0) {
                callback.stream(chunk(id, choiceStop("timeout")));
                break;
            } else {
                LOGGER.debug("duration: {}", duration);
                try {
                    callback.stream(chunk(id, choiceContent(duration.toString() + "\n")));
                    Thread.sleep(INTERVAL);
                } catch (Exception e) {
                    callback.failed(e);
                    return;
                }
            }
        }
        callback.completed(null);
    }

    private ChatCompletionChunk chunk(final String id, final ChatChunkChoice choice) {
        final ChatCompletionChunk chunk = new ChatCompletionChunk();
        chunk.setId(id);
        chunk.setChoices(Collections.singletonList(choice));
        return chunk;
    }

    private ChatChunkChoice choiceStop(final String finishReason) {
        return choice(finishReason);
    }

    private ChatChunkChoice choiceContent(final String content) {
        return choice(deltaContent(content));
    }

    private ChatChunkChoice choice(final String finishReason) {
        final ChatChunkChoice choice = new ChatChunkChoice();
        choice.setFinishReason(finishReason);
        return choice;
    }


    private ChatChunkChoice choice(final Message delta) {
        final ChatChunkChoice choice = new ChatChunkChoice();
        choice.setDelta(delta);
        return choice;
    }

    private Message deltaContent(final String content) {
        final Message delta = new Message();
        delta.setContent(content);
        return delta;
    }
}
