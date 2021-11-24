package org.shortln.tools;

import lombok.*;
import org.shortln.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

public class SessionTool {
    @Data
    @Builder
    public static class CurAccount {
        private Long id;
        private String username;
    }

    @Getter @Setter
    public static HttpSession session;

    public static void setSession(
            Long id, String username
    ) {
        session.setAttribute("user", new HashMap<>() {{
            put("id", id);
            put("username", username);
        }});
    }

    public static CurAccount curAccount() {
        var u = (HashMap<String, Object>) session.getAttribute("user");
        if (u == null)
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "请登陆。");
        try {
            return CurAccount.builder()
                    .id((Long) u.get("id"))
                    .username((String) u.get("username"))
                    .build();
        } catch (Exception e) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "服务器数据有误，请重新登陆。");
        }
    }
}
