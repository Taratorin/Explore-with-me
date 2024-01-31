package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.dto.ApiError;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({ConstraintViolationException.class, BadRequestException.class})
    public ResponseEntity<ApiError> handleConstraintViolationException(final RuntimeException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        HandleError result = getHandleError(e, httpStatus);
        return ResponseEntity.status(result.httpStatus).body(result.error);
    }

    @ExceptionHandler()
    public ResponseEntity<ApiError> handleNotFoundException(final NotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        HandleError result = getHandleError(e, httpStatus);
        return ResponseEntity.status(result.httpStatus).body(result.error);
    }

    @ExceptionHandler()
    public ResponseEntity<ApiError> handleNotFoundException(final ConflictException e) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        HandleError result = getHandleError(e, httpStatus);
        return ResponseEntity.status(result.httpStatus).body(result.error);
    }

    private static HandleError getHandleError(Exception e, HttpStatus httpStatus) {
        List<String> stackTraceElements = new ArrayList<>();
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            stackTraceElements.add(stackTraceElement.toString());
        }
        ApiError error = ApiError.builder()
                .errors(stackTraceElements)
                .message(e.getLocalizedMessage())
                .status(httpStatus)
                .build();
        return new HandleError(httpStatus, error);
    }

    private static class HandleError {
        public final HttpStatus httpStatus;
        public final ApiError error;

        public HandleError(HttpStatus httpStatus, ApiError error) {
            this.httpStatus = httpStatus;
            this.error = error;
        }
    }
}