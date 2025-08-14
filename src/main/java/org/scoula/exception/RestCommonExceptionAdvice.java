package org.scoula.exception;

import lombok.extern.log4j.Log4j2;
import org.scoula.common.response.CommonResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = {"org.scoula", "org.scoula.auth", "org.scoula.member", "org.scoula.house", "org.scoula.summary"})
@Log4j2
@Order(1)
public class RestCommonExceptionAdvice {
    // 400 Bad Request: 이메일로 유저를 찾지 못한 경우
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Map<String, Object>>  handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.response(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        log.error("API 예외 처리 발생", e);
        Map<String, Object> body = new HashMap<>();
        body.put("status", 500);
        body.put("error", "Internal Server Error");
        body.put("message", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handle404(HttpServletRequest request, NoHandlerFoundException e) {
        log.error("API 404 Not Found", e);
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Not Found");
        body.put("uri", request.getRequestURI());
        body.put("message", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleRse(ResponseStatusException ex,
                                                         HttpServletRequest req) {
        Map<String, Object> body = Map.of(
                "status", ex.getStatus().value(),
                "error", ex.getStatus().getReasonPhrase(),
                "message", ex.getReason(),
                "path", req.getRequestURI(),
                "timestamp", Instant.now().toString()
        );
        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNpe(NullPointerException ex) {
        return ResponseEntity.badRequest()
       .body(Map.of("status",400, "error","invalid request", "message", ex.getMessage()));
    }

}
