package com.situ.kdz2026.config;

import com.situ.kdz2026.util.JsonResult;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 当控制器中的方法出现参数校验异常时，即会调用此方法响应值。
     *
     * @param ex 参数校验异常
     * @return 响应结果
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<JsonResult<?>> handle(HandlerMethodValidationException ex) {
        String msg = ex.getAllErrors().stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(JsonResult.fail(msg));
    }

    /**
     * 需要同时监听HandlerMethodValidationException和MethodArgumentNotValidException，二者都可能会出现
     * 两个是完全不同的异常类型，继承体系结构也不一样，没办法合并为一个。只是恰巧都包含getAllErrors方法而已
     *
     * @param ex 参数校验异常
     * @return 响应结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonResult<?>> handle(MethodArgumentNotValidException ex) {
        String msg = ex.getAllErrors().stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(JsonResult.fail(msg));
    }
}
