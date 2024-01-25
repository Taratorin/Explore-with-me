//package ru.practicum.exception;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//import ru.practicum.dto.ApiError;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.ConstraintViolationException;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//@Slf4j
//@RestControllerAdvice
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//
////    @ExceptionHandler({IllegalArgumentException.class})
////    public ResponseEntity<ApiError> handleBadRequest(Exception ex) throws IOException {
////        ApiError apiError = new ApiError(
////                HttpStatus.BAD_REQUEST,
////                "Данные в реквесте невалидны",
////                ex.getLocalizedMessage(),
////                Collections.singletonList(error(ex))
////        );
////        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
////    }
//
//    @ExceptionHandler()
//    public ResponseEntity<ApiError> handleConstraintViolation(Exception ex) {
//        List<String> errors = new ArrayList<>();
//
//        ApiError apiError = new ApiError(
//                HttpStatus.FORBIDDEN,
//                "Данные в реквесте невалидны",
//                ex.getLocalizedMessage(),
//                errors
//        );
//        return ResponseEntity.status(apiError.getStatus()).body(apiError);
//    }
//
//
//    private String error(Exception e) throws IOException {
//        StringWriter sw = new StringWriter();
//        PrintWriter pw = new PrintWriter(sw);
//        e.printStackTrace(pw);
//        String error = sw.toString();
//        sw.close();
//        pw.close();
//        return error;
//    }
//}
