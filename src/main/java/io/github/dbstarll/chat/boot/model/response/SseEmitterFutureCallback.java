package io.github.dbstarll.chat.boot.model.response;

import io.github.dbstarll.utils.net.api.StreamFutureCallback;
import io.github.dbstarll.utils.openai.model.api.BaseCompletion;
import io.github.dbstarll.utils.openai.model.fragment.Choice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import java.io.IOException;

public class SseEmitterFutureCallback<C extends Choice, T extends BaseCompletion<C>>
        implements StreamFutureCallback<T> {
    public enum State {
        STREAM, DONE, ERROR, CANCEL
    }

    public static final class DateWithState<E> {
        private final State state;
        private final E data;

        private DateWithState(final State state, final E data) {
            this.state = state;
            this.data = data;
        }

        /**
         * get state.
         *
         * @return state
         */
        public State getState() {
            return state;
        }

        /**
         * get data.
         *
         * @return data
         */
        public E getData() {
            return data;
        }
    }

    private final SseEmitter emitter;

    /**
     * StreamFutureCallback for SseEmitter.
     *
     * @param emitter SseEmitter
     */
    public SseEmitterFutureCallback(final SseEmitter emitter) {
        this.emitter = emitter;
    }

    @Override
    public void stream(final T result) {
        send(State.STREAM, result.getChoices(), result.getId());
    }

    @Override
    public void completed(final Void result) {
        send(State.DONE, null, null);
        emitter.complete();
    }

    @Override
    public void failed(final Exception ex) {
        try {
            send(State.ERROR, ex.getMessage(), null);
        } catch (Exception e) {
            // ignore
        } finally {
            emitter.completeWithError(ex);
        }
    }

    @Override
    public void cancelled() {
        send(State.CANCEL, null, null);
        emitter.complete();
    }

    private <E> void send(final State state, final E data, final String id) {
        final SseEventBuilder builder = SseEmitter.event()
                .data(new DateWithState<>(state, data), MediaType.APPLICATION_JSON);
        if (StringUtils.isNotBlank(id)) {
            builder.id(id);
        }
        try {
            emitter.send(builder);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
