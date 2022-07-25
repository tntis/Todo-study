package jocture.todo.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)// Jpa 스팩..
@AllArgsConstructor(access = AccessLevel.PRIVATE)// Builder에서 필요
@Builder
@Getter
@ToString
public class User {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String username;
//    memberName

    private String email;

    private String password;
}

