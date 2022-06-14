package jocture.todo.dto.response;

/* Generic 문자 ( 관례)
 E - Element
 N - Number
 T - Type
 K - Key
 V - Value
*/

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 불변(Immutable) 객체로 만드는게 좋다. => setter 메소드는 없어야한다.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter // Serialize : Json객체를 String으로 변황
public class ResponseDto<T> {

    private static final String SUCCESS_CODE = "0000";
    private static final String SUCCESS_MESSAGE = "성공";

    private String code;
    private String message;
    private ResponseResultDto<T> result; // Association 관계(연관관계) -> Aggregation(집약관계)
    // result 에는 단일값
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
        //  Composition(합성관계)

        return new ResponseDto<>(SUCCESS_CODE, SUCCESS_MESSAGE, result);
    }

   /* public static <T> ResponseDto<T> of(String code, String message, T result) {
        return new ResponseDto<>(code, message, result);
    }*/
}
    /* 정적 팩토리 메서드의 명명 방식 관례
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