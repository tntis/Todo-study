package jocture.todo.controller;

import jocture.todo.dto.UserDto;
import jocture.todo.dto.response.ResponseDto;
import jocture.todo.dto.response.ResponseResultDto;
import jocture.todo.entity.User;
import jocture.todo.mapper.UserMapper;
import jocture.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController // @Controller + @ResponseBody
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto<UserDto>> signUp(
            @RequestBody UserDto userDto
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
    @PostMapping("/login")
    public ResponseDto<UserDto> logIn(
            @RequestBody UserDto userDto
    ) {
        log.debug(">>> userDto : {}", userDto);
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        User user = userService.login(email, password);


      /*  UserDto responceUserDto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();*/

        ResponseResultDto<UserDto> responseDto = ResponseResultDto.of(userMapper.toDto(user));

        return ResponseDto.of(responseDto);
    }
}
