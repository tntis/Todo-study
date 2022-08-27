package jocture.todo.data.dto;

import jocture.todo.web.controller.validation.marker.TodoValidationGroup;
import jocture.todo.data.entity.Todo;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Builder // 다른생성자가 없으면, 전체 파라미터
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class TodoDto {

    @NotNull(groups = {TodoValidationGroup.Update.class, TodoValidationGroup.Deletion.class})
    private Integer id;

    @NotBlank(groups = {TodoValidationGroup.Creation.class, TodoValidationGroup.Update.class})
    private String title;

    private boolean done;

    // 인스턴스(Instance) 메소드
    //  인스턴스 -> 객체(new 클래스)
    // 정적(static) 메소드
    public static TodoDto toDto(Todo dto) {
        return TodoDto.builder().id(dto.getId()).title(dto.getTitle()).done(dto.isDone()).build();
    }

    public static List<TodoDto> todoDtoList(List<Todo> todos) {
        return todos.stream()
                //.parallel()// 병렬로 동시에 실행되는 문 (단점 : 순서보장이 안됨)
                .peek(t -> log.info("todo : {}", t))
                .map(TodoDto::toDto)
                .peek(t -> log.info("TodoDto : {}", t))
                .collect(Collectors.toList());

        /*
        List<TodoDto> todoDtos = new ArrayList<>();
        for (Todo todo : todos) {
            todoDtos.add(TodoDto.toDto(todo));
        }
*/

    }
}

/*
 *
 *
 *
 * */
