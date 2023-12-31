package com.eazybytes.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
// Using the MappedSuperclass strategy, inheritance is only evident in the class but not the entity model.
// Let’s start by creating a Person class that will represent a parent class:
//
//@MappedSuperclass
//public class Person {
//
//    @Id
//    private long personId;
//    private String name;
//
//    // constructor, getters, setters
//}
// Notice that this class no longer has an @Entity annotation, as it won’t be persisted in the database by itself.
//  Next, let’s add an Employee subclass:
//
//@Entity
//public class MyEmployee extends Person {
//    private String company;
//    // constructor, getters, setters
//}
// In the database, this will correspond to one MyEmployee table with three columns for the declared and inherited fields of the subclass.
// If we’re using this strategy, ancestors cannot contain associations with other entities.

//In simple terms, mappedSuperClass fields won't be persisted into db by themselves, but will be persisetd by the extending class

@Getter @Setter @ToString
@EntityListeners(AuditingEntityListener.class) // -> auditing related
public class BaseEntity {

//    @CreatedDate, @CreatedBy, @LastModifiedDate, @LastModifiedBy
//    all the above annotations are provided by spring data
//    by using these annos we're saying spring framework that, please update these fields with appropriate values
//    when the record has been created/updated


    @Column(updatable = false)
//    @Column(updatable = false) -> tells spring data jpa that
//    whenever a record is updated this field should not be updated(uneffected)
//    this field will be given a value when a new record is inserted, and in future the same record is
//    updated this should not be updated
//    in simple words createdAt means when the record is created.
    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @Column(insertable = false)
//    @Column(insertable = false) -> this tells to spring data jpa
//    that whenever a new record is inserted this field should be null
//    this field will be given a value only when a record is updated.
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(insertable = false)
    private String updatedBy;

}
