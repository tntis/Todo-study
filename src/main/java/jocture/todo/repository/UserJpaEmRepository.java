package jocture.todo.repository;

import jocture.todo.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor // final 로 선언된 생성자를 주입함
public class UserJpaEmRepository implements UserRepository {

    // 컴포넌트 주입방법 : 생성자, setter, 필드 주입
    private final EntityManager em;


    @Override
    public void save(User user) {
        em.persist(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String jpql = "select u from User u where email = :email";
        try {
            User user = em.createQuery(jpql, User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Boolean existByEmail(String email) {
        String jpql = "select 1 from User u where email = :email";
        try {
            Integer result = em.createQuery(jpql, Integer.class).setParameter("email", email).getSingleResult();
            return result != null;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        String jpql = "select u from User u where email = :email and password = :password";
        try {
            User user = em.createQuery(jpql, User.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(String userId) {
        User user = em.find(User.class, userId);
        return Optional.ofNullable(user);

    }
}
