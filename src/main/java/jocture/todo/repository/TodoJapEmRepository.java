package jocture.todo.repository;

import jocture.todo.entity.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TodoJapEmRepository implements TodoRepository{

    // @PersistenceContext
    private final EntityManager em;

    //@Autowired
//    public TodoJapEmRepository(EntityManager em) {
//        this.em = em;
//    }

    @Override
    public List<Todo> findAll() {
        String jpql = "select t from Todo t"; // Java Persistence Query Language
        return em.createQuery(jpql, Todo.class)
                .getResultList();
    }

    @Override
    public Optional<Todo> findById(Integer id) {
        String jpql = "select t from Todo t where t.id = :id";
        try{

            Todo todo =  em.createQuery(jpql, Todo.class)
                    .setParameter("id",id)
                    .getSingleResult();
            return Optional.of(todo);
           // return Optional.ofNullable(todo);
        }catch (NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Todo> findByUserId(String userId) {
        String jpql = "select t from Todo t where t.userId = :userId";
            return  em.createQuery(jpql, Todo.class)
                    .setParameter("userId",userId)
                    .getResultList();
    }

    @Override
    public void save(Todo todo) {
        em.persist(todo);

    }

    // TDD(Test-Driven Development) -> 텍스트 주도 개발
   // -> 실제 메인 코드보다 테스트 코드를 먼저 만드는 개발방법론

    @Override
    public void delete(Todo todo) {
        Optional<Todo> result = findById(todo.getId());
        if(result.isPresent()){
            em.remove(todo);
        }
    }

    @Override
    public void deleteById(Integer id) {
        Optional<Todo> result = findById(id);
        if(result.isPresent()){
            Todo todo = result.get();
            delete(todo);
        }
    }
    public void delete1(Todo todo) {
        deleteById1(todo.getId());
    }

    public void deleteById1(Integer id) {
        Optional<Todo> result = findById(id);
        if(result.isPresent()){
            Todo todo = result.get();
            em.remove(todo);
        }
    }

    public void delete2(Todo todo) {
        deleteById1(todo.getId());
    }

    public void deleteById2(Integer id) { // Java 에서는 Method Signature (매소드 시그니쳐)
       // Worst
        /*
        Optional<Todo> result = findById(id);
        if(result.isPresent()){
            Todo todo = result.get();
            em.remove(todo);
        }
        */
        // Not bad
        //findById(id).ifPresent(entity -> em.remove(entity));

        // Best ( Method Reference)
        findById(id).ifPresent(em::remove); //  t -> em.remove(t)  null 일수 없다.
    }

}
