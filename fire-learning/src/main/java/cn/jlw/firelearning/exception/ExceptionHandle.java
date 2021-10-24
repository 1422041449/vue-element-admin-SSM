package cn.jlw.firelearning.exception;

import cn.jlw.firelearning.model.LeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@Slf4j
@ControllerAdvice
public class ExceptionHandle {
    /**
     * 处理数据校验异常
     *
     * @param validException instanceof BindException,MethodArgumentNotValidException
     * @return Result
     */
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    @ResponseBody
    public LeResponse<?> validExceptionHandler(Exception validException) {

        BindingResult result;
        if (validException instanceof BindException) {
            result = ((BindException) validException).getBindingResult();
        } else {
            result = ((MethodArgumentNotValidException) validException).getBindingResult();
        }
        StringBuilder message = new StringBuilder();
        if (result.hasFieldErrors()) {
            message.append(result.getFieldErrors().get(0).getDefaultMessage());
            result.getFieldErrors().forEach(error -> log.error(error.getField() + ":" + error.getDefaultMessage()));
        } else {
            message.append(result.getAllErrors().get(0).getDefaultMessage());
            result.getAllErrors().forEach(error -> log.error(error.getObjectName() + ":" + error.getDefaultMessage()));
        }
        return LeResponse.fail(message.toString());
    }

    /**
     * 处理所有自定义异常
     */
    @ExceptionHandler(LeException.class)
    @ResponseBody
    public LeResponse<?> handleCustomException(LeException e) {
        log.error(e.getMessage());
        return LeResponse.fail(e.getMessage());
    }

    /**
     * runtime异常
     *
     * @param e error
     * @return Object
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public LeResponse handle(Exception e) {
        if (e instanceof LeException) {
            LeException he = (LeException) e;
            log.error("业务异常{}", e.getMessage());
            return LeResponse.fail(he.getMessage());
        }
        
        BindingResult result;
        if (e instanceof BindException) {
            result = ((BindException) e).getBindingResult();
            return LeResponse.fail(Objects.requireNonNull(result.getFieldError().getDefaultMessage()));
        } else if (e instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException valid = (MissingServletRequestParameterException) e;
            return LeResponse.fail(Objects.requireNonNull(valid.getMessage()));
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException valid = (MethodArgumentNotValidException) e;
            BindingResult bindingResult = valid.getBindingResult();
            return LeResponse.fail(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            return LeResponse.fail(e.getMessage());
        } else {
            System.err.println(e.getMessage());
            e.printStackTrace();
            log.error("系统异常{}", e.getMessage());
            return LeResponse.fail();
        }
    }
}
