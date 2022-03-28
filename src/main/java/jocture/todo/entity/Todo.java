package jocture.todo.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/*
    JPA
    -> java의 표준 ORM
    -> 스펙, 규격, 표준
    -> 구현체가 필요하다
        -> Hibernate(디폴트)

   스프링 데이터 JPA : 스프링 진영에서 지원하는 라이브러리
 */
 
@Entity
// @Table(name = "tbl_todo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo {

    @Id @GeneratedValue
    private Integer id;

    private String userId;

    private String title;

    private boolean done;


}
