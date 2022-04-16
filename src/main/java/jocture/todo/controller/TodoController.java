package jocture.todo.controller;

import jocture.todo.dto.TodoDto;
import jocture.todo.entity.Todo;
import jocture.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 스프링 3계층(레이어) -> @Controller, @service, @Repository

@Slf4j
//@Controller + @ResponseBody
@RestController
@RequiredArgsConstructor
public class TodoController {

    private static final String TEMP_USER_ID = "temp";

    private final TodoService service;

    // HTTP Request Method : GET(조회), POST(등록/만능), PUT(전체수정), PATCH(부분수정), DELETE(삭제)
    // API 요소 : HTTP 요청 메소드 + URI Path (+ 요청 파라미터 + 여청바디 + 응답 바디)

    @GetMapping("/todo")
    public ResponseEntity<List<TodoDto>> getTodoList() {  //ResponseEntity<?>
        List<Todo> todos = service.getList(TEMP_USER_ID);
        // JSON -> 객체를 표현하는 String
        // 객체를 JSON 스트링으로 변환 -> HttpMessageConverter (Serialize/Serializer)
        // JSON 스트링을 객체로 변황 -> HttpMessageConverter (Deserialize/Deserializer)
        return ResponseEntity.ok().body(TodoDto.todoDtoList(todos));
    }

    @PostMapping("/todo")
    public ResponseEntity<List<TodoDto>> createTodo(
            @RequestBody Todo todo
    ) {
        log.info(">>>> todo : {}", todo);
        todo.setUserId(TEMP_USER_ID);
        //  Todo todo = Todo.builder().userId(TEMP_USER_ID).title(title).build();
        service.create(todo);

        return getTodoList();
    }

    @PutMapping("/todo")
    public ResponseEntity<List<TodoDto>> updateTodo(
            Integer id, String title, boolean done
    ) {
        Todo todo = Todo.builder().id(id).userId(TEMP_USER_ID).title(title).done(done).build();
        service.update(todo);

        return getTodoList();
    }

    @DeleteMapping("/todo")
    public ResponseEntity<List<TodoDto>> deleteTodo(
            Integer id
    ) {
        Todo todo = Todo.builder().userId(TEMP_USER_ID).id(id).build();
        service.delete(todo);
        return getTodoList();
    }
}
