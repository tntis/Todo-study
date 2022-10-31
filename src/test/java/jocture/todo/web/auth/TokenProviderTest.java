package jocture.todo.web.auth;

import jocture.todo.data.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TokenProviderTest {

    TokenProvider tokenProvider = new TokenProvider();
    static String TEST_USER_ID = "tntt";
    static String createdToken;

    //@Order(1)
    @Test
    void create() {
        User user = User.builder()
                .id(TEST_USER_ID)
                .email("tn@gmail.com")
                .build();

        createdToken = tokenProvider.create(user);

        System.out.println("token =::" + createdToken);
        Assertions.assertThat(createdToken).isNotEmpty();
    }

    //@Order(2)
    @Test
    void validateAndGetUserId() {
        // given
        //String token = createdToken;
        create();

        // when
        String userId = tokenProvider.validateAndGetUserId(createdToken);
        //then
        System.out.println("userId = " + userId);
        Assertions.assertThat(userId).isEqualTo(TEST_USER_ID);
    }

}