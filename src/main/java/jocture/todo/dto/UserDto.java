package jocture.todo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jocture.todo.controller.validation.marker.UserVailidationGroup;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor // 당장은 다른 생성자가 없을 때도, 정의해 줘야 나중에 오루 발생 가능성이 줄어든다.
@AllArgsConstructor(access = AccessLevel.PRIVATE) // builder를 위해 필요
@JsonInclude(JsonInclude.Include.NON_NULL)   // jackson 라이브러리가 디시리얼라이즈,시리얼라이즈를 해줌 (객체를 json String 으로변환 해쥬는 : 시리얼라이즈)  
public class UserDto {

    @NotBlank(groups = {UserVailidationGroup.Signup.class})
    private String username;

    @Email(groups = {UserVailidationGroup.Signup.class, UserVailidationGroup.Login.class})
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "올바른 패스워드 형식이 아닙니다.(대소문자 + 숫자조합)", // 국체화 (i18n)
            groups = {UserVailidationGroup.Signup.class})
    @NotEmpty(groups = {UserVailidationGroup.Login.class})
    private String password;


    private String token; //jwt

    // 생성자 정의를 하지 않으면 , 기본생성자(파라미터가 없는 생성자)가 자동으로 생성만들어진다.


}
