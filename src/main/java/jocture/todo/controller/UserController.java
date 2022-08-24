package jocture.todo.controller;

import jocture.todo.controller.session.SessionConst;
import jocture.todo.controller.session.SessionManager;
import jocture.todo.controller.validation.marker.UserVailidationGroup;
import jocture.todo.dto.UserDto;
import jocture.todo.dto.response.ResponseDto;
import jocture.todo.dto.response.ResponseResultDto;
import jocture.todo.entity.User;
import jocture.todo.mapper.UserMapper;
import jocture.todo.service.UserService;
import jocture.todo.type.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController // @Controller + @ResponseBody
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final SessionManager sessionManager;

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto<UserDto>> signUp(
            @RequestBody @Validated({UserVailidationGroup.Signup.class}) UserDto userDto
            // MappingJackson2HttpMessageConverter : Deserialize: 객체생성(디폴트생성자) -> getter/setter 메서드를 이용해 프로퍼티 찾아서 Reflection을 이용해 할당
    ) {
        log.debug(">>> userDto : {}", userDto);


        // UserDto -> User  변환
       /* User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();*/

        //   UserMapperImpl userMapper = new UserMapperImpl();

        User user = userMapper.toEntity(userDto);
        log.debug(">>> user2 : {}", user);

        // Service Layer(서비스 계층)에 위임
        userService.signUp(user);

        // 타입 추론 : 컴파일러가 빌드 타립을 추론할 수 있어야 한다.
        // 대표적인게 람다식
        //Java 10 애서 var 키워드 추가(Kotlin 언어에서 쓰는 방식)

        // 응답
        UserDto responceUserDto = userMapper.toDto(user);
       /* var responceUserDto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();*/


        //ResponseResultPageDto dataPage = ResponseResultPageDto.of(1, 20, 15);
        ResponseResultDto<UserDto> responseDto = ResponseResultDto.of(responceUserDto /* , dataPage*/);

        return ResponseDto.responseEntityof(responseDto);
    }
    // entity 객체를 퍼시스트할떄?

    // setter 와 builder의 관계성?.. 찾아보기
/*
    // 해당 컨트럴러에서 쓰고 던져 컨트롤러 사용하려면 컨텍스트핸들러
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> applicationExcetionHandler(Exception e) { // Throwable
        log.error("public ResponseEntity<?> applicationExcetionHandler -> ", e);
        return ResponseDto.responseEntityof(ResponseCode.BAD_REQUEST, e.getMessage());

    }

    @ExceptionHandler
    public ResponseEntity<?> excetionHandler(Exception e) { // Throwable
        log.error("exceptionHandler -> ", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }
*/
    @PostMapping("/login/v1")
    public ResponseDto<UserDto> logInV1(
            @RequestBody @Validated({UserVailidationGroup.Login.class}) UserDto userDto,
            HttpServletResponse response
    ) {
        log.debug(">>> userDto : {}", userDto);
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        User user = userService.login(email, password);
        Cookie idCookie = new Cookie("userId", user.getId());
        idCookie.setPath("/");
        response.addCookie(idCookie);

        return ResponseDto.of(ResponseCode.SUCCESS);
    }

    @PostMapping("/login/v2")
    public ResponseDto<UserDto> logInV2(
            @RequestBody @Validated({UserVailidationGroup.Login.class}) UserDto userDto,
            HttpServletResponse response
    ) {
        log.debug(">>> userDto : {}", userDto);
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        User user = userService.login(email, password);

        sessionManager.createSession(user, response);
        return ResponseDto.of(ResponseCode.SUCCESS);
    }

    @PostMapping("/login/v3")
    public ResponseDto<UserDto> logInV3(
            @RequestBody @Validated({UserVailidationGroup.Login.class}) UserDto userDto,
            HttpServletRequest request
    ) {
        log.debug(">>> userDto : {}", userDto);
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        User user = userService.login(email, password);

        HttpSession session = request.getSession(); // 세션 생성
        session.setAttribute(SessionConst.SESSION_USER_KEY, user); // 세션에 데이터 저장
        session.setMaxInactiveInterval(5);
        return ResponseDto.of(ResponseCode.SUCCESS);
    }

    @PostMapping("/logout/v1")
    public ResponseDto<UserDto> logOutV1(HttpServletResponse response) {
        Cookie idCookie = new Cookie("userId", "");
        idCookie.setPath("/");
        idCookie.setMaxAge(0);
        response.addCookie(idCookie);
        return ResponseDto.of(ResponseCode.SUCCESS);
    }

    @PostMapping("/logout/v2")
    public ResponseDto<UserDto> logOutV2(HttpServletRequest request) {
        sessionManager.expireSession(request);
        return ResponseDto.of(ResponseCode.SUCCESS);
    }

    @PostMapping("/logout/v3")
    public ResponseDto<UserDto> logOutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseDto.of(ResponseCode.SUCCESS);
    }


}
