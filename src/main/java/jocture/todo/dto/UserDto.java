package jocture.todo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor // 당장은 다른 생성자가 없을 때도, 정의해 줘야 나중에 오루 발생 가능성이 줄어든다.
@AllArgsConstructor(access = AccessLevel.PRIVATE) // builder를 위해 필요
@JsonInclude(JsonInclude.Include.NON_NULL)   // jackson 라이브러리가 디시리얼라이즈,시리얼라이즈를 해줌
public class UserDto {

    private String id;
    private String username;
    private String email;
    private String password;
    private String token; //jwt

    // 생성자 정의를 하지 않으면 , 기본생성자(파라미터가 없는 생성자)가 자동으로 생성만들어진다.


}
