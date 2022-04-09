package jocture.todo.service;

import jocture.todo.entity.Todo;
import jocture.todo.exception.ApplicationException;
import jocture.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//계층형 아키텍쳐(Layered Architure)
// -> 스프링 기본적으로 3계층을 제공
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository repository;

    public List<Todo> getList(String userId){
        return repository.findByUserId(userId);
    }
    @Transactional
    public void create(Todo todo){
        validateTodo(todo);

        repository.save(todo);
        log.info("Todo가 등록되었습니다. {}", todo.getId());
    }

    private void validateTodo(Todo todo) {
        if(todo == null){
            String msg = "Todo가 null 입니다.";
            log.error(msg);
            throw new ApplicationException(msg);
        }
        if(todo.getUserId() == null){
            String msg2 = "Todo의 UserId가 Null 입니다";
            log.error(msg2);
            throw new ApplicationException(msg2);
        }
    }
    @Transactional
    public void update(Todo newTodo){
        log.debug(">>> newTodo : {}", newTodo);
        validateTodo(newTodo);
        Optional<Todo> oldTodo =  repository.findById(newTodo.getId());
        log.debug(">>> oldTodo : {}", oldTodo);
        oldTodo.ifPresentOrElse(todo -> {
            todo.setTitle(newTodo.getTitle());
            todo.setDone(newTodo.isDone());
           // repository.save(todo); // 없어도 무관하게 동작함
            log.info("Todo가 수정되었습니다.{}", todo.getId());
        }, () -> log.warn("수정할 Todo가 없습니다.{}", newTodo.getId()));
    }
    @Transactional
    public void delete(Todo todo){
        log.debug(">>> todo : {}", todo);
        validateTodo(todo);

        repository.delete(todo);
        log.info("Todo가 삭제되었습니다.{}", todo.getId());
    }
}
