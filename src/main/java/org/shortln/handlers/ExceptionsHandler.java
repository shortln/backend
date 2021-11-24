package org.shortln.handlers;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.shortln.CallFunByEnv;
import org.shortln.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Builder
class Detail {
    public String message;
    public List<StackTraceElement> stackTraces;
}

@Slf4j
@RestControllerAdvice
public class ExceptionsHandler {
    @Autowired
    private CallFunByEnv<BusinessException, List> callFunByEnv;

    @ExceptionHandler(BusinessException.class)
    public Detail runtimeExceptionHandler(
            HttpServletResponse response, BusinessException exception
    ) {
        // log.error("运行时异常：{}", exception.getMessage());
        exception.printStackTrace();

        response.setStatus(exception.getStatusCode());
        var d = Detail.builder().message(exception.getMessage()).build();
        callFunByEnv.run(new HashMap<>() {{
            put("dev", exception -> d.stackTraces = Arrays.asList(exception.getStackTrace()));
            put("pro", exception -> null);
        }}, exception);
        return d;
    }
}
