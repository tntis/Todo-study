package jocture.todo.controller.session;

import jocture.todo.data.entity.User;
import jocture.todo.web.controller.session.SessionManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void testSessionManager() {
        // HttpServletResponse 객체 Mocking
        MockHttpServletResponse response = new MockHttpServletResponse();
        User user = User.builder().build();
        // 세션 생성
        sessionManager.createSession(user, response);

        // kHttpServletRequest 객체 Mocking
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        // 세션 조회
        Object result = sessionManager.getSession(request);
        Assertions.assertThat(result).isEqualTo(user);

        // 세션 제거
        sessionManager.expireSession(request);
        Object result2 = sessionManager.getSession(request);
        Assertions.assertThat(result2).isNull();
    }


}