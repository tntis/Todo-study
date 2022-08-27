package jocture.todo.service;

import jocture.todo.data.entity.User;
import jocture.todo.exception.ApplicationException;
import jocture.todo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

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
        assertThatThrownBy(() -> service.signUp(user))
                .isInstanceOf(ApplicationException.class);

    }

    @Test
    void signUp_emailIsNull() {
        //Given
        String email = null;
        String password = "qweqwe";
        User user = buildUser(email, password);
        // When & Then
        assertThatThrownBy(() -> service.signUp(user))
                .isInstanceOf(ApplicationException.class);

    }

    @Test
    void signUp_emaiAlreayExists() {
        //Given
        String email = "tn@asd.com";
        String password = "qweqwe";
        User user = buildUser(email, password);
        // Method Mocking
        doReturn(true).when(repository).existByEmail(any());
        // When & Then
        assertThatThrownBy(() -> service.signUp(user))
                .isInstanceOf(ApplicationException.class);

    }

    @Test
    void signUp_success() {
        //Given
        String email = "tn@asd.com";
        String password = "qweqwe";
        User user = buildUser(email, password);
        log.info(">>> repository : {}", repository);

        // When
        service.signUp(user);
        // Then
        Mockito.verify(repository, Mockito.times(1)).save(user);

    }

    private User buildUser(String email, String password) {
        return User.builder()
                .username("tn")
                .email(email)
                .password(password)
                .build();
    }

    @Test
    void login() {
        //Given
        String email = "tn@asd.com";
        String password = "qweqwe";
        User user = buildUser(email, password);
        doReturn(Optional.of(user))
                .when(repository)
                .findByEmailAndPassword(email, password);
        //When
        User result = service.login(email, password);  // Test Driven Development

        //Then
        assertThat(result).isEqualTo(user).isSameAs(user);
    }

    @Test
    void login_Nodata() {
        //Given
        String email = "tn@asd.com";
        String password = "qweqwe";
        doReturn(Optional.empty())
                .when(repository)
                .findByEmailAndPassword(email, password);
        //When
        //Then
        // assertThatThrownBy(()-> );
    }

    @ParameterizedTest(name = "[{index}] {0} is blank value") // {index} {0 : paramper}
    @ValueSource(strings = {" ", "  ", "\n", "\r"})
    @NullAndEmptySource
        // @Nullable + @EmptySource
    void login_Nomail(String email) {
        //Given
        String password = "qweqwe";
        //When
        //Then
        assertThatThrownBy(() -> service.login(email, password))
                .isInstanceOf(ApplicationException.class);

    }

    @ParameterizedTest(name = "[{index}] {0} is blank value")
    @ValueSource(strings = {" ", "  ", "\n", "\r"})
    @NullAndEmptySource
    void login_NoPassword(String password) {
        //Given
        String email = "tn@asd.com";
        //When
        //Then
        assertThatThrownBy(() -> service.login(email, password))
                .isInstanceOf(ApplicationException.class);
    }

}