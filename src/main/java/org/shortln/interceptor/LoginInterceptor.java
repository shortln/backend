package org.shortln.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.shortln.annotations.NeedLogin;
import org.shortln.exceptions.BusinessException;
import org.shortln.tools.SessionTool;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler
    ) {
        if (handler instanceof HandlerMethod) {
            // 设置当前会话 session
            SessionTool.setSession(request.getSession());

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.getMethodAnnotation(NeedLogin.class) != null) {
                SessionTool.curAccount();
            }
        }
        return true;
    }
}
