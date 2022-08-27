package jocture.todo.service;

import jocture.todo.data.entity.Todo;
import jocture.todo.exception.ApplicationException;
import jocture.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional // @SpringBootTest의 @Transactional의 기본설정은 Rollback
class TodoServiceTest {

    @Autowired
    TodoService service;
    private static final String USER_NAME = "temp";

    @Autowired
    TodoRepository repository;

    @Autowired
    EntityManager em;


    @Test
    void getList() {
        //Given
        int createCount = 3;
        createTodoList(createCount);
        System.out.println("=========================== 1");
        // When
        List<Todo> todos = service.getList(USER_NAME);
        System.out.println("=========================== 2");
        // Then
        assertThat(todos).hasSize(3);
    }

    private void createTodoList(int createCount) {
        for (int i = 0; i < createCount; i++) {
            Todo todo = Todo.builder().title("밥먹기" + i).userId(USER_NAME).build();
            service.create(todo);
        }
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

    @Test
    void create_invalidEntity() {
        // Given
        Todo todo = null;
        // when
        ThrowableAssert.ThrowingCallable callable = () -> service.create(todo);
        Executable executable = () -> service.create(todo);
        // then
        assertThatThrownBy(callable).isInstanceOf(ApplicationException.class);
        Assertions.assertThrows(ApplicationException.class, executable);
    }


    @Test
    @DisplayName("Todo에 USERID 없을떄")
    void create_withoutUserId() {
        // Given
        Todo todo = Todo.builder().title("밥먹기").build();
        // when
        ThrowableAssert.ThrowingCallable callable = () -> service.create(todo);
        Executable executable = () -> service.create(todo);
        // then
        assertThatThrownBy(callable).isInstanceOf(ApplicationException.class);
        Assertions.assertThrows(ApplicationException.class, executable);
    }

    @Test
    void update() {
        //Given
        int createCount = 3;
        createTodoList(createCount);
        List<Todo> todos = service.getList(USER_NAME);
        Todo newTodo = todos.get(1);
        newTodo.setDone(true);
        newTodo.setTitle("자바공부 안하기");
        // When
        service.update(newTodo);
        // Then
        Optional<Todo> result = repository.findById(newTodo.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo(newTodo.getTitle());
        assertThat(result.get().isDone()).isEqualTo(newTodo.isDone());


        // 비영속
        Todo todo = Todo.builder().title("밥먹기").userId("ee").build();
        todo.setDone(true);
        // em.
    }

    @Test
    void update_Noresult() {
        //Given
        Todo todo = null;
        // when
        ThrowableAssert.ThrowingCallable callable = () -> service.update(todo);
        Executable executable = () -> service.update(todo);
        // then
        assertThatThrownBy(callable).isInstanceOf(ApplicationException.class);
        Assertions.assertThrows(ApplicationException.class, executable);
    }

    @Test
    void update_NouserID() {
        // Given
        Todo todo = Todo.builder().title("밥먹기").build();
        // when
        ThrowableAssert.ThrowingCallable callable = () -> service.update(todo);
        Executable executable = () -> service.update(todo);
        // then
        assertThatThrownBy(callable).isInstanceOf(ApplicationException.class);
        Assertions.assertThrows(ApplicationException.class, executable);
    }


    @Test
    void delete() {
        //Given
        int createCount = 3;
        createTodoList(createCount);
        List<Todo> todos = service.getList(USER_NAME);
        Todo deletingTodo = todos.get(1); // 영송상태
        System.out.println("deletingTodo = " + deletingTodo);
        // When
        service.delete(deletingTodo);
        // Then
        Optional<Todo> result = repository.findById(deletingTodo.getId());
        assertThat(result).isEmpty();
        System.out.println("deletingTodo2222 = " + deletingTodo); // 비영속 상태
        deletingTodo.setDone(true); // 객체를 수정
        em.flush();

    }

    @Test
    void delete_invalidEntity() {
        //Given
        Todo todo = Todo.builder().title("놀러가기").userId(USER_NAME).build();
        service.create(todo);
        // When
        service.delete(todo);
        // Then
        Optional<Todo> result = repository.findById(todo.getId());
        assertThat(result).isEmpty();
    }

}


// list 의 종류가 많으니