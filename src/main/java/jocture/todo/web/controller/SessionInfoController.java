package jocture.todo.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

// JavaEE 스펙 정의 : 이 스펙 중에서 Servlet Container 구현체가 Tomcat
// (대부분) JavaSE 면 충분하다.

@Slf4j
@RestController
public class SessionInfoController {
    @GetMapping("/sessions")
    public String getSessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        if (session == null) {
            return "No Session";
        }

        // 세션 데이터 출력
        log.debug(">>> sessionId ={}", session.getId());
        log.debug(">>> MaxInactiveInterval ={}", session.getMaxInactiveInterval());
        log.debug(">>> CreationTime ={}", getFormattedDateTime(session.getCreationTime()));
        log.debug(">>> LastAccessedTime ={}", getFormattedDateTime(session.getLastAccessedTime()));
        log.debug(">>> isNew ={}", session.isNew());

        return "OK";
    }

    private String getFormattedDateTime(long epochMilli) {
        //of : 정적 팩토리 메서드 패턴
        return Instant.ofEpochMilli(epochMilli)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ;
    }
}
