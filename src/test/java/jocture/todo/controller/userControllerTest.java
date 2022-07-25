package jocture.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jocture.todo.dto.UserDto;
import jocture.todo.entity.User;
import jocture.todo.exception.ApplicationException;
import jocture.todo.exception.LoginFailException;
import jocture.todo.mapper.UserMapper;
import jocture.todo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest({UserController.class, UserMapper.class})
class userControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    // Overloading : 동일한 메서드사용  같으나 파라미터가 다른경우( 파라미터 수, 파라미터 타입)
    // Overriding : 메서드 재정의(상속관계에서 필요)

    // 메서드 시그니쳐 : 메서드명 + 파라미터 타입(Overloading시 구분) -> 런타임 시에 컴파일러가 구분할수 있느냐?
    // -> 잘못 설명한 부분 (시그니쳐X) : 반환타입, throws

    @Test
    void signUp_seccess() throws Exception {
        //given
        String url = "/auth/signup";
        UserDto userDto = UserDto.builder()
                .username("test")
                .email("tn@asd.com")
                .password("PaSswords4")
                .build();

        String body = objectMapper.writeValueAsString(userDto);
        //메서드 모킹
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            User user = (User) args[0];
            ReflectionTestUtils.setField(user, "id", "asd");
            return null;
        }).when(userService).signUp(any());

        // when & then
        MvcResult mvcResult = mvc.perform(
                        post(url).content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // .andExpect(status().is(200))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        log.debug(">>>> responseBody  : {}  ", responseBody);
    }

    @Test
    void signUp_validationFail_email() throws Exception {
        //given
        String url = "/auth/signup";
        UserDto userDto = UserDto.builder()
                .username("test")
                .email("tnasd.com")
                .password("PaSswords4")
                .build();

        String body = objectMapper.writeValueAsString(userDto);

        // when & then
        mvc.perform(post(url).content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void signUp_validationFail_username(String username) throws Exception {
        //given
        String url = "/auth/signup";
        UserDto userDto = UserDto.builder()
                .username(username)
                .email("tna@sd.com")
                .password("PaSswords4")
                .build();

        String body = objectMapper.writeValueAsString(userDto);

        // when & then
        mvc.perform(post(url).content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_validationFail_password() throws Exception {
        //given
        String url = "/auth/signup";
        UserDto userDto = UserDto.builder()
                .username("test")
                .email("tna@sd.com")
                .password("PaSswords") // Invalid pattern
                .build();

        String body = objectMapper.writeValueAsString(userDto);

        // when & then
        mvc.perform(post(url).content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    void signUp_exception() throws Exception {
        //given
        String url = "/auth/signup";
        UserDto userDto = UserDto.builder()
                .email("tn@asd.com")
                .username("test")
                .password("PaSsword3")
                .build();

        String body = objectMapper.writeValueAsString(userDto);
        // 메서드 모킹
        doThrow(new ApplicationException("ex message"))
                .when(userService)
                .signUp(any());

        // when
        MvcResult mvcResult = mvc.perform(
                        post(url).content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                // .andDo(print())
                // .andExpect(status().is(200))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void logIn_seccess() throws Exception {
        //given
        String url = "/auth/login";
        UserDto userDto = UserDto.builder()
                .email("tn@asd.com")
                .password("PaSsword5")
                .build();

        String body = objectMapper.writeValueAsString(userDto);
        //메서드 모킹
        doReturn(User.builder()
                .id("sert")
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .build()).when(userService).login(anyString(), any());

        // when & then
        MvcResult mvcResult = mvc.perform(
                        post(url).content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // .andExpect(status().is(200))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        log.debug(">>>> responseBody  : {}  ", responseBody);
    }

    @Test
    void logIn_validationFail_email() throws Exception {
        //given
        String url = "/auth/login";
        UserDto userDto = UserDto.builder()
                .email("tnasd.com")
                .password("PaSsword5")
                .build();

        String body = objectMapper.writeValueAsString(userDto);

        // when & then
        mvc.perform(post(url).content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void logIn_validationFail_password(String pass) throws Exception {
        //given
        String url = "/auth/login";
        UserDto userDto = UserDto.builder()
                .email("tn@asd.com")
                .password(pass)
                .build();

        String body = objectMapper.writeValueAsString(userDto);

        // when & then
        mvc.perform(post(url).content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void logIn_Fail() throws Exception {
        //given
        String url = "/auth/login";
        UserDto userDto = UserDto.builder()
                .email("tn@asd.com")
                .password("apdpdpdW")
                .build();

        String body = objectMapper.writeValueAsString(userDto);

        doThrow(new LoginFailException("아이디 또는 패스워드가 잘못되었습니다."))
                .when(userService).login(anyString(), anyString());

        // when & then
        mvc.perform(post(url).content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());


    }


}


/**
 * HTTP 상태 코드
 * 400 Bad Request
 * 클라이언트가 잘못된 요청을 해서 서버가 요청을 처리할 수 없음
 * 요청 구문, 메시지 등등 오류
 * 클라이언트는 요청 내용을 다시 검토하고, 보내야함
 * 예) 요청 파라미터가 잘못되거나, API 스펙이 맞지 않을 때
 * 401 Unauthorized
 * 라이언트가 해당 리소스에 대한 인증이 필요함
 * 인증(Authentication) 되지 않음
 * 401 오류 발생시 응답에 WWW-Authenticate 헤더와 함께 인증 방법을 설명
 * 참고
 * 인증(Authentication): 본인이 누구인지 확인, (로그인)
 * 인가(Authorization): 권한부여 (ADMIN 권한처럼 특정 리소스에 접근할 수 있는 권한, 인증이 있어야 인가가 있음)
 * 오류 메시지가 Unauthorized 이지만 인증 되지 않음
 * 403 Forbidden
 * 서버가 요청을 이해했지만 승인을 거부함
 * 주로 인증 자격 증명은 있지만, 접근 권한이 불충분한 경우
 * 예) 어드민 등급이 아닌 사용자가 로그인은 했지만, 어드민 등급의 리소스에 접근하는 경우
 * 404 Not Found
 * 요청 리소스를 찾을 수 없음
 * 요청 리소스가 서버에 없음
 * 또는 클라이언트가 권한이 부족한 리소스에 접근할 때 해당 리소스를 숨기고 싶을 때
 */
// handler