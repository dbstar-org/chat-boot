package io.github.dbstarll.chat.boot.event;

import io.github.dbstarll.chat.boot.model.request.Question;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public final class StreamChatEvent extends ApplicationEvent {
    private final Question question;
    private final transient SseEmitter emitter;

    /**
     * 构造StreamChatEvent.
     *
     * @param question Question
     * @param emitter  SseEmitter
     */
    public StreamChatEvent(final Question question, final SseEmitter emitter) {
        super(question);
        this.question = question;
        this.emitter = emitter;
    }

    Question getQuestion() {
        return question;
    }

    SseEmitter getEmitter() {
        return emitter;
    }
}
