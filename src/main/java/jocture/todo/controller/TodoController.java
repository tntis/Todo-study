package jocture.todo.controller;

import jocture.todo.dto.TodoCreateDto;
import jocture.todo.dto.TodoDeleteDto;
import jocture.todo.dto.TodoDto;
import jocture.todo.dto.TodoUpdateDto;
import jocture.todo.dto.response.ResponseDto;
import jocture.todo.dto.response.ResponseResultDto;
import jocture.todo.entity.Todo;
import jocture.todo.mapper.TodoMapper;
import jocture.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// 스프링 3계층(레이어) -> @Controller, @service, @Repository

@Slf4j
//@Controller + @ResponseBody
@RestController
@RequiredArgsConstructor
//@CrossOrigin(orgins = {"http://localhost:3000"}, maxAge = 3600, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}) // Wedmvcconfig
@RequestMapping("/todo")
public class TodoController {

    private static final String TEMP_USER_ID = "temp";

    private final TodoService service;
    private final TodoMapper todoMapper;

    // HTTP Request Method : GET(조회), POST(등록/만능), PUT(전체수정), PATCH(부분수정), DELETE(삭제)
    // API 요소 : HTTP 요청 메소드 + URI Path (+ 요청 파라미터 + 여청바디 + 응답 바디)

    //@CrossOrigin("*")
    @GetMapping
    public ResponseDto<List<TodoDto>> getTodoList() {  //ResponseEntity<?>
        List<Todo> todos = service.getList(TEMP_USER_ID);
        // JSON -> 객체를 표현하는 String
        // 객체를 JSON 스트링으로 변환 -> HttpMessageConverter (Serialize/Serializer)
        // JSON 스트링을 객체로 변황 -> HttpMessageConverter (Deserialize/Deserializer)

        ResponseResultDto<List<TodoDto>> restponceData = ResponseResultDto.of(todoMapper.toDtoList(todos));
        return ResponseDto.of(restponceData);
    }

    @PostMapping
    public ResponseDto<List<TodoDto>> createTodo(
            @RequestBody @Valid TodoCreateDto todoDto
    ) {
        log.info(">>>> todo : {}", todoDto);
        // Todo todo = Todo.from(todoDto); // TodoDto 를 todo 객체로 변환한다.

        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUserId(TEMP_USER_ID);
        service.create(todo);

        return getTodoList();
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String createTodo222(
            @RequestBody String Body
    ) {
        log.info(">>>> Body : {}", Body);

        return "{\"title\":\"테스트\"}";
    }

    @PutMapping
    public ResponseDto<List<TodoDto>> updateTodo(
            @RequestBody @Valid TodoUpdateDto todoDto
    ) {
        // Todo todo = Todo.from(todoDto);
        log.info(">>>> updateTodo :: {}", todoDto);
        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUserId(TEMP_USER_ID);
        service.update(todo);

        return getTodoList();
    }

    @DeleteMapping
    public ResponseDto<List<TodoDto>> deleteTodo(
            @RequestBody @Valid TodoDeleteDto todoDto
    ) {
        //  Todo todo = Todo.from(todoDto);
        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUserId(TEMP_USER_ID);
        service.delete(todo);
        return getTodoList();
    }
}
