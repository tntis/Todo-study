package jocture.todo.controller;

import jocture.todo.controller.validation.marker.TodoValidationGroup;
import jocture.todo.dto.TodoDto;
import jocture.todo.dto.response.ResponseDto;
import jocture.todo.dto.response.ResponseResultDto;
import jocture.todo.entity.Todo;
import jocture.todo.exception.NoAuthenticationException;
import jocture.todo.mapper.TodoMapper;
import jocture.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// 스프링 3계층(레이어) -> @Controller, @service, @Repository

@Slf4j
//@Controller + @ResponseBody
@RestController
@RequiredArgsConstructor
//@CrossOrigin(orgins = {"http://localhost:3000"}, maxAge = 3600, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}) // Wedmvcconfig
@RequestMapping("/todo")
public class TodoController {

    private final TodoService service;
    private final TodoMapper todoMapper;

    // HTTP Request Method : GET(조회), POST(등록/만능), PUT(전체수정), PATCH(부분수정), DELETE(삭제)
    // API 요소 : HTTP 요청 메소드 + URI Path (+ 요청 파라미터 + 여청바디 + 응답 바디)

    @GetMapping("/v1") // 사용 노노
    public ResponseDto<List<TodoDto>> getTodoList(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String userId = Arrays.stream(Optional.ofNullable(cookies).orElse(new Cookie[]{}))
                .filter(cookie -> cookie.getName().equals("userId"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
        log.debug(">>> userId : {}", userId);
        return getRealTodoList(userId);
    }

    @GetMapping("/v2")
    public ResponseDto<List<TodoDto>> getTodoList(
            @CookieValue(name = "userId", required = false) String userId
    ) {
        log.debug(">>> userId : {}", userId);
        validateUser(userId);
        return getRealTodoList(userId);
    }

    private void validateUser(String userId) {
        if (userId == null) {
            throw new NoAuthenticationException("로그인을 하셔야 합니다.");
        }
    }


    //@CrossOrigin("*")
    @GetMapping
    public ResponseDto<List<TodoDto>> getRealTodoList(String userId) {  //ResponseEntity<?>
        List<Todo> todos = service.getList(userId);
        // JSON -> 객체를 표현하는 String
        // 객체를 JSON 스트링으로 변환 -> HttpMessageConverter (Serialize/Serializer)
        // JSON 스트링을 객체로 변황 -> HttpMessageConverter (Deserialize/Deserializer)

        ResponseResultDto<List<TodoDto>> restponceData = ResponseResultDto.of(todoMapper.toDtoList(todos));
        return ResponseDto.of(restponceData);
    }

    @PostMapping
    public ResponseDto<List<TodoDto>> createTodo(
            @RequestBody @Validated(TodoValidationGroup.Creation.class) TodoDto todoDto,
            @CookieValue(name = "userId", required = false) String userId

    ) {
        log.info(">>>> todo : {}", todoDto);
        // Todo todo = Todo.from(todoDto); // TodoDto 를 todo 객체로 변환한다.
        validateUser(userId);


        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUserId(userId);
        service.create(todo);

        return getRealTodoList(userId);
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
            @RequestBody @Validated(TodoValidationGroup.Update.class) TodoDto todoDto
            //@Valid 자바에서 제공
            , @CookieValue(name = "userId", required = false) String userId
    ) {
        // Todo todo = Todo.from(todoDto);
        log.info(">>>> updateTodo :: {}", todoDto);
        validateUser(userId);
        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUserId(userId);
        service.update(todo);

        return getRealTodoList(userId);
    }

    @DeleteMapping
    public ResponseDto<List<TodoDto>> deleteTodo(
            @RequestBody @Validated(TodoValidationGroup.Deletion.class) TodoDto todoDto
            , @CookieValue(name = "userId", required = false) String userId
    ) {
        validateUser(userId);
        Todo todo = todoMapper.toEntity(todoDto);
        todo.setUserId(userId);
        service.delete(todo);
        return getRealTodoList(userId);
    }
}
