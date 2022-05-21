package jocture.todo.repository;

import jocture.todo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class UserJpaEmRepositoryTest {
    @Autowired
    UserJpaEmRepository repository;

    @Test
    void basic() {

        //Given
        User user = buildUser();
        // When
        repository.save(user);
        Optional<User> result = repository.findByEmail(EMAIL);
        Boolean exist = repository.existByEmail(EMAIL);

        // Then
        assertThat(result).isPresent()
                .contains(user)    // equals() 비교
                .containsSame(user);   // == 비교
        // assertThat(result.get()).isEqualTo(user); // equals() 비교
        // assertThat(result.get()).isSameAs(user); // == 비교

        assertThat(exist).isTrue();
    }

    final String EMAIL = "tn@asc.com";

    private User buildUser() {
        User user = User.builder()
                .username("tn")
                .email(EMAIL)
                .password("qwerr")
                .build();
        return user;
    }

    @Test
    void findByEmailAndPassword() {
        //Given
        User user = buildUser();
        repository.save(user);
        // When
        Optional<User> result = repository.findByEmailAndPassword(EMAIL, "qwerr");

        //Then
        assertThat(result).isPresent()
                .contains(user)
                .containsSame(user);
    }
}