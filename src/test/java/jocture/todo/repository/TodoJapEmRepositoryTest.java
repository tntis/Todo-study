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
import java.util.Objects;
import java.util.Optional;

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
        Todo todo1 = saveTodo("밥먹기");
        Todo todo2 = saveTodo("청소하기");
        //when
        List<Todo> all = repository.findAll();

        //then
        Assertions.assertThat(all).hasSize(2);
        Assertions.assertThat(all).filteredOn(m -> Objects.equals(m, todo1)).isSameAs(todo1);

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
        Todo todo1 = saveTodo("밥먹기");
        Todo todo2 = saveTodo("청소하기");
        //when
       Optional<Todo> result = repository.findById(todo1.getId());

        //then
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getId()).isEqualTo(todo1.getId());
    }


    @Test
    void findById_NoResult() {
        //given
        Integer id = -999;

        //when
        Optional<Todo> result = repository.findById(id);

        //then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void findByUserId() {
        //given
        Todo todo1 = saveTodo("배고파");

        //when
        List<Todo> result = repository.findByUserId(todo1.getUserId());

        //then
        Assertions.assertThat(result).hasSize(1);


    }

    @Test
    @Rollback(value = false)
    void save() {
        log.info("repository : {}" , repository);
        // Given
        Todo todo1 = Todo.builder().userId(USER_NAME).title("자바 공부하기").done(false).build();
        Todo todo2 = Todo.builder().userId(USER_NAME).title("자바 공부하기2").done(false).build();

        // When
        repository.save(todo1);
        repository.save(todo2);
      //  em.flush();

        // Then
        Assertions.assertThat(repository.findById(todo1.getId())).isPresent();


    }

    @Test
    void delete(){
        // Given
        Todo todo1 = saveTodo("배고파");

        // When
        repository.delete(todo1);

        // Then
        Optional<Todo> result = repository.findById(todo1.getId());
        Assertions.assertThat(result).isEmpty();
    }


    @Test
    void deleteById(){
        // Given
        Todo todo1 = saveTodo("배고파");

        // When
        repository.deleteById(todo1.getId());

        // Then
        Optional<Todo> result = repository.findById(todo1.getId());
        Assertions.assertThat(result).isEmpty();
    }


}