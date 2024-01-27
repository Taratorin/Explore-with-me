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

    @ExceptionHandler()
    public ResponseEntity<ApiError> handleConstraintViolationException(final ConstraintViolationException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        HandleError result = getHandleError(e, httpStatus);
        return ResponseEntity.status(result.httpStatus).body(result.error);
    }

    @ExceptionHandler()
    public ResponseEntity<ApiError> handleBadRequestException(final BadRequestException e) {
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

//    @ExceptionHandler({MethodArgumentNotValidException.class, BadRequestException.class})
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleBadRequestException(final BadRequestException e) {
//        log.trace("Получен статус 400 Bad request {}", e.getMessage(), e);
//        return new ErrorResponse(e.getMessage());
//    }
//
//
//    @ExceptionHandler()
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handleNotFoundException(final NotFoundException e) {
//        log.trace("Получен статус 404 Not Found {}", e.getMessage(), e);
//        return new ErrorResponse(e.getMessage());
//    }
//
//    @ExceptionHandler()
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ErrorResponse handleForbiddenException(final ForbiddenException e) {
//        log.trace("Получен статус 403 Forbidden {}", e.getMessage(), e);
//        return new ErrorResponse(e.getMessage());
//    }

//    @ExceptionHandler()
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ResponseEntity<ApiError> handleThrowableException(final Throwable e) {
//        log.trace("Получен статус 500 Internal Server Error {}", e.getMessage(), e);
//        ApiError error = ApiError.builder()
//                .errors(Collections.singletonList(e.getMessage()))
//                .reason(e.getLocalizedMessage())
//                .status(HttpStatus.NO_CONTENT)
//                .build();
//        return ResponseEntity.status(400).body(error);
//    }
}