package jocture.todo.dto.response;

/* Generic 문자 ( 관례)
 E - Element
 N - Number
 T - Type
 K - Key
 V - Value
*/

import com.fasterxml.jackson.annotation.JsonInclude;
import jocture.todo.type.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

// 불변(Immutable) 객체로 만드는게 좋다. => setter 메소드는 없어야한다.
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter // Serialize : Json객체를 String으로 변황
public class ResponseDto<T> {

   /* private static final String SUCCESS_MESSAGE = "성공";
    public static final String BAD_REQUEST_MESSAGE = "잘못된 요청";*/

    private String code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResponseResultDto<T> result; // Association 관계(연관관계)
    // result 에는 단일값

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ResponseErrorDto> errors;

    private ResponseDto(String code, String message, ResponseResultDto<T> result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    private ResponseDto(String code, String message, ResponseErrorDto error) {
        this.code = code;
        this.message = message;
        addError(error);
    }

    private ResponseDto(String code, String message, List<ResponseErrorDto> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    private ResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /*
// 전체 생성자를 선언 해줬기 떄문에 지워도됨

    private ResponseDto(String code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }
*/
    // 메소드명 이같음, 파라미터 수 또는 파라미토 타입이 다르다 (메서드 시그니쳐가 다르다) -> Method Overloading

    // 정적 팩토리 메서드(Static Factory Method ) 방식이 대체로 더 좋다.
    /*public static <T> ResponseDto<T> of(T result) {
        return new ResponseDto<>(SUCCESS_CODE, SUCCESS_MESSAGE, result);
    }

     */
    public static <T> ResponseDto<T> of(ResponseResultDto<T> result) {

        return new ResponseDto<>(ResponseCode.SUCCESS.code(), ResponseCode.SUCCESS.getMessage(), result);
    }

    public static <T> ResponseEntity<ResponseDto<T>> responseEntityof(ResponseResultDto<T> result) {
        return ResponseEntity.ok(
                new ResponseDto<>(ResponseCode.SUCCESS.code(), ResponseCode.SUCCESS.getMessage(), result));
    }

    public static <T> ResponseEntity<ResponseDto<T>> responseEntityof(ResponseCode responseCode) {
        HttpStatus httpStatus = responseCode.getHttpStatus();
        return ResponseEntity.status(httpStatus).body(
                new ResponseDto<>(responseCode.code(), responseCode.getMessage()));
    }

    public static <T> ResponseEntity<ResponseDto<T>> responseEntityof(ResponseCode responseCode, ResponseResultDto<T> result) {
        HttpStatus httpStatus = responseCode.getHttpStatus();
        return ResponseEntity.status(httpStatus).body(
                new ResponseDto<>(responseCode.code(), responseCode.getMessage(), result));
    }

    public static <T> ResponseEntity<ResponseDto<T>> responseEntityof(ResponseCode responseCode, ResponseErrorDto error) {
        HttpStatus httpStatus = responseCode.getHttpStatus();
        return ResponseEntity.status(httpStatus).body(
                new ResponseDto<>(responseCode.code(), responseCode.getMessage(), error));
    }

    public static <T> ResponseEntity<ResponseDto<T>> responseEntityof(ResponseCode responseCode, List<ResponseErrorDto> errors) {
        HttpStatus httpStatus = responseCode.getHttpStatus();
        return ResponseEntity.status(httpStatus).body(
                new ResponseDto<>(responseCode.code(), responseCode.getMessage(), errors));
    }

    // 인스턴스 메서드(vs. 스태틱 메서드
    public void addError(ResponseErrorDto error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
    }
}
    /*  팩토리 메서드의 명명 방식 관례
    - from
        - 하나의 매개변수를 받아 해당 타입의 인스턴스를 반환하는 형변환 메서드
    - of
        - 여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메서드
    - valueOf
        - `from`과 `of`의 더 자세한 버전
    - instance or getInstance
        - (매개변수가 있다면) 매개변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스 미보장
    - create or newInstance
        - `instance` or `getInstance`와 같지만, 매번 새로운 인스턴스 보장
    - getType
        - `getInstance`와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 사용
        - "`Type`"은 팩터리 메서드가 반환할 객체의 타입
    - newType
        - `newInstance`와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 사용
        - "`Type`"은 팩터리 메서드가 반환할 객체의 타입
    - type
        - `getType` or `newType`의 간결한 버전
     */