package jocture.todo.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
/*
    JPA
    -> java의 표준 ORM
    -> 스펙, 규격, 표준
    -> 구현체가 필요하다
        -> Hibernate(디폴트)

   스프링 데이터 JPA : 스프링 진영에서 지원하는 라이브러리
 */

@Entity
// @Table(name = "tbl_todo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
public class Todo {

    private static final String TEMP_USER_ID = "temp";
    @Id
    @GeneratedValue
    private Integer id;

    private String userId;

    private String title;
    // @Temporal
    private LocalDateTime createdAt;

    private boolean done;

   /* public static Todo from(TodoDto dto) {
        return Todo.builder()
                .id(dto.getId())
                .userId(TEMP_USER_ID)
                .title(dto.getTitle())
                .done(dto.isDone())
                .build();
    }*/

}
//@JsonIgnore