package jocture.todo.web.controller.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    private final Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    public void createSession(Object value, HttpServletResponse response) {
        // 세션 스토어 세션ID-세션값을 저장
        // 서버에서만 일어나는 일이므로 클라이언트가 알지 못함
        String sessionID = UUID.randomUUID().toString();
        sessionStore.put(sessionID, value);

        // 클라이언트와 상태 유지를 하기 위해, 쿠키로 세선ID 전달(세션값X)
        Cookie sessionCookie = new Cookie(SessionConst.SESSION_ID, sessionID);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);

        // 쿠키 구분
        // 영속 쿠키 : 만료 날짜가 있는 것 (만료날짜까지 쿠키 유지)
        // 세선 쿠키 : 만료 날짜가 없는 것 (브라우저 종료 시까지 유지)

        // HTTP 프로토콜
        // Stateless(무상태)

        // 로그인 과정에 대해 아는대로 설명해보세요
        // Http 특송(무결정), 세션 개념, 쿠키(Request/Response) 등//

    }

    public Object getSession(HttpServletRequest request) {
        return getSessionCookie(request)
                // 세션ID 값으로 세션 스토어에서 세션ID에 저장된 세션값 조회
                .map(sessionStore::get)
                .orElse(null);

        // 세션 스토어에서 세션ID에 저장된 세션값을 찾는다다
    }

    private Optional<String> getSessionCookie(HttpServletRequest request) {
        Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElseGet(() -> new Cookie[]{});
        return Arrays.stream(cookies)
                // 쿠키에서 세션 ID 조회
                .filter(c -> c.getName().equals(SessionConst.SESSION_ID))
                .findFirst()
                // 세션ID 쿠키에서 세션 ID 값조회
                .map(Cookie::getValue);
    }

    public void expireSession(HttpServletRequest request) {
        Optional<String> cookie = getSessionCookie(request);

        cookie.ifPresent(sessionStore::remove);

    }
}
