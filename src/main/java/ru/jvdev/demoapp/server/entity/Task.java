package ru.jvdev.demoapp.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by ilshat on 04.08.16.
 */
@Entity
@Data
@EqualsAndHashCode(exclude = "user")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank
    private String title;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // required for JPA
    public Task() {}

    public Task(String title) {
        this.title = title;
    }
}
