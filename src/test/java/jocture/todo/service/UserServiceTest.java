package jocture.todo.service;

import jocture.todo.entity.User;
import jocture.todo.exception.ApplicationException;
import jocture.todo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserService service;

    @Mock // face-double, Mock, Stub -> 가짜
    UserRepository repository;

    @Test
    void signUp_userIsNull() {
        //Given
        User user = null;
        // When & Then
        Assertions.assertThatThrownBy(() -> service.signUp(user))
                .isInstanceOf(ApplicationException.class);

    }

    @Test
    void signUp_emailIsNull() {
        //Given
        String email = null;
        User user = buildUser(email);
        // When & Then
        Assertions.assertThatThrownBy(() -> service.signUp(user))
                .isInstanceOf(ApplicationException.class);

    }

    @Test
    void signUp_emaiAlreayExists() {
        //Given
        String email = "tn@asd.com";
        User user = buildUser(email);
        // Method Mocking
        Mockito.doReturn(true).when(repository).existByEmail(ArgumentMatchers.any());
        // When & Then
        Assertions.assertThatThrownBy(() -> service.signUp(user))
                .isInstanceOf(ApplicationException.class);

    }

    @Test
    void signUp_success() {
        //Given
        String email = "tn@asd.com";
        User user = buildUser(email);
        log.info(">>> repository : {}", repository);

        // When
        service.signUp(user);
        // Then
        Mockito.verify(repository, Mockito.times(1)).save(user);

    }

    private User buildUser(String email) {
        return User.builder()
                .username("tn")
                .email(email)
                .password("qwerr")
                .build();
    }
}