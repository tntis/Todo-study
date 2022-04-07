package jocture.todo.service;

import jocture.todo.entity.Todo;
import jocture.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional // @SpringBootTest의 @Transactional의 기본설정은 Rollback
class TodoServiceTest {

    @Autowired TodoService service;
    private static final String USER_NAME = "temp";

    @Autowired
    TodoRepository repository;


    //@Test
    void getList() {
    }

    @Test
    void create() {
        // Given
        Todo todo = Todo.builder().title("밥먹기").userId(USER_NAME).build();
        // when
        service.create(todo);
        // then
        Optional<Todo> result = repository.findById(todo.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isPositive();
        assertThat(result.get().getTitle()).isEqualTo(todo.getTitle());

    }

    //@Test
    void update() {
    }

    //@Test
    void delete() {
    }
}