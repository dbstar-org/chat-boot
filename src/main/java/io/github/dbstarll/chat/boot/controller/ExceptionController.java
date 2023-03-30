package io.github.dbstarll.chat.boot.controller;

import io.github.dbstarll.utils.openai.ApiErrorException;
import io.github.dbstarll.utils.openai.model.response.ApiError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@ControllerAdvice
class ExceptionController {
    @ResponseBody
    @ExceptionHandler(ApiErrorException.class)
    ApiError validateErrorHandler(final ApiErrorException e, final HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return e.getApiError();
    }
}
