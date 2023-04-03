package io.github.dbstarll.chat.boot.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Async
@Component
class StreamChatEventListener implements ApplicationListener<StreamChatEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamChatEventListener.class);

    @Override
    public void onApplicationEvent(final StreamChatEvent event) {
        LOGGER.info("onApplicationEvent: {}", event.getQuestion());
    }
}
