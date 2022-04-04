package jocture.todo.repository;

import jocture.todo.entity.Todo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class TodoJapEmRepositoryTest {

    private static final String USER_NAME = "temp";

    @Autowired
    TodoRepository repository;
    @Autowired
    EntityManager em;

    @Test
    void findAll() {
        //given
        Todo todo1 = Todo.builder().userId(USER_NAME).title("밥먹기").done(false).build();
        Todo todo2 = Todo.builder().userId(USER_NAME).title("청소하기").done(false).build();
        repository.save(todo1);
        repository.save(todo2);

        //when
        List<Todo> all = repository.findAll();

        //then
        Assertions.assertThat(todo1);
    }

    private Todo saveTodo(String title) {
        // Given
        Todo todo = Todo.builder() // 빌더 패턴
                .userId(USER_NAME)
                .title(title)
                .build(); // build/out 디렉토리에 자동 생성되는 코드 구조 확인하기!
        repository.save(todo);
        return todo;
    }

    @Test
    void findById() {
        //given
        //when
        //then
    }


    @Test
    void findById_NoResult() {
        //given
        //when
        //then
    }

    @Test
    void findByUserId() {
        //given
        //when
        //then
    }

    @Test
    @Rollback(value = false)
    void save() {
        log.info("repository : {}" , repository);
        // Given
        Todo todo1 = Todo.builder().userId(USER_NAME).title("자바 공부하기").done(false).build();
        Todo todo2 = Todo.builder().userId(USER_NAME).title("자바 공부하기2").done(false).build();

        // When
        log.info("11111");
        repository.save(todo1);
        log.info("222");
        repository.save(todo2);
      //  em.flush();

        // Then

    }




}