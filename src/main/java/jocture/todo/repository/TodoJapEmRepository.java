package jocture.todo.repository;

import jocture.todo.entity.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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
}
