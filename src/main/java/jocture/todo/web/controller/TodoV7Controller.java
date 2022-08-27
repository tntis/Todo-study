package jocture.todo.web.controller;

import jocture.todo.data.dto.TodoDto;
import jocture.todo.data.dto.response.ResponseDto;
import jocture.todo.data.dto.response.ResponseResultDto;
import jocture.todo.data.entity.Todo;
import jocture.todo.data.entity.User;
import jocture.todo.data.mapper.TodoMapper;
import jocture.todo.service.TodoService;
import jocture.todo.service.UserService;
import jocture.todo.web.argument.LoginUser;
import jocture.todo.web.controller.session.SessionManager;
import jocture.todo.web.controller.validation.marker.TodoValidationGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/todo/v7")
public class TodoV7Controller {

    private final TodoService todoService;
    private final UserService userService;
    private final TodoMapper todoMapper;
    private final SessionManager sessionManager;

    @GetMapping
    public ResponseDto<List<TodoDto>> getTodoListV6(
            @LoginUser User loginUser
    ) {
        log.debug(">>>> loginUser {}", loginUser);
        return getRealTodoList(loginUser.getId());
    }

    private ResponseDto<List<TodoDto>> getRealTodoList(String userId) {  //ResponseEntity<?>
        List<Todo> todos = todoService.getList(userId);
        // JSON -> 객체를 표현하는 String
        // 객체를 JSON 스트링으로 변환 -> HttpMessageConverter (Serialize/Serializer)
        // JSON 스트링을 객체로 변황 -> HttpMessageConverter (Deserialize/Deserializer)

        ResponseResultDto<List<TodoDto>> restponceData = ResponseResultDto.of(todoMapper.toDtoList(todos));
        return ResponseDto.of(restponceData);
    }

    @PostMapping
    public ResponseDto<List<TodoDto>> createTodo(
            @RequestBody @Validated(TodoValidationGroup.Creation.class) TodoDto todoDto,
            @LoginUser User loginUser

    ) {
        log.info(">>>> todo : {}", todoDto);
        // Todo todo = Todo.from(todoDto); // TodoDto 를 todo 객체로 변환한다.

        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUserId(loginUser.getId());
        todoService.create(todo);

        return getRealTodoList(loginUser.getId());
    }

    @PutMapping
    public ResponseDto<List<TodoDto>> updateTodo(
            @RequestBody @Validated(TodoValidationGroup.Update.class) TodoDto todoDto
            //@Valid 자바에서 제공
            , @LoginUser User loginUser
    ) {
        // Todo todo = Todo.from(todoDto);
        log.info(">>>> updateTodo :: {}", todoDto);
        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUserId(loginUser.getId());
        todoService.update(todo);

        return getRealTodoList(loginUser.getId());
    }

    @DeleteMapping
    public ResponseDto<List<TodoDto>> deleteTodo(
            @RequestBody @Validated(TodoValidationGroup.Deletion.class) TodoDto todoDto
            , @LoginUser User loginUser
    ) {
        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUserId(loginUser.getId());
        todoService.delete(todo);
        return getRealTodoList(loginUser.getId());
    }
}
