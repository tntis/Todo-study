package jocture.todo.controller;

import jocture.todo.dto.TodoDto;
import jocture.todo.entity.Todo;
import jocture.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 스프링 3계층(레이어) -> @Controller, @service, @Repository

@Slf4j
//@Controller + @ResponseBody
@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:3000"}, maxAge = 3600, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}) // Wedmvcconfig
public class TodoController {

    private static final String TEMP_USER_ID = "temp";

    private final TodoService service;

    // HTTP Request Method : GET(조회), POST(등록/만능), PUT(전체수정), PATCH(부분수정), DELETE(삭제)
    // API 요소 : HTTP 요청 메소드 + URI Path (+ 요청 파라미터 + 여청바디 + 응답 바디)

    //@CrossOrigin("*")
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
            @RequestBody TodoDto todoDto
    ) {
        log.info(">>>> todo : {}", todoDto);
        Todo todo = Todo.from(todoDto);
        service.create(todo);

        return getTodoList();
    }

    @PostMapping(value = "/todo", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String createTodo222(
            @RequestBody String Body
    ) {
        log.info(">>>> Body : {}", Body);

        return "{\"title\":\"테스트\"}";
    }

    @PutMapping("/todo")
    public ResponseEntity<List<TodoDto>> updateTodo(
            @RequestBody TodoDto todoDto
    ) {
        Todo todo = Todo.from(todoDto);
        service.update(todo);

        return getTodoList();
    }

    @DeleteMapping("/todo")
    public ResponseEntity<List<TodoDto>> deleteTodo(
            @RequestBody TodoDto todoDto
    ) {
        Todo todo = Todo.from(todoDto);
        service.delete(todo);
        return getTodoList();
    }
}
