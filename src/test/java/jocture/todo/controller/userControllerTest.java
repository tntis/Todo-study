package jocture.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jocture.todo.dto.UserDto;
import jocture.todo.entity.User;
import jocture.todo.exception.ApplicationException;
import jocture.todo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
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
@WebMvcTest(UserController.class)
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
                .password("PaSs")
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
    void signUp_exception() throws Exception {
        //given
        String url = "/auth/signup";
        UserDto userDto = UserDto.builder()
                .email("tn@asd.com")
                .username("test")
                .password("PaSs")
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
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void logIn_seccess() throws Exception {
        //given
        String url = "/auth/login";
        UserDto userDto = UserDto.builder()
                .email("tn@asd.com")
                .password("PaSs")
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

}

// handler