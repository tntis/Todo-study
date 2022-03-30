package jocture.todo.repository;

import jocture.todo.entity.Todo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

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
    }

    @Test
    void findById() {
    }

    @Test
    void findByUserId() {
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