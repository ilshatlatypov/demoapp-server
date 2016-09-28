package ru.jvdev.demoapp.server.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(exclude = "tasks")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z]+")
    private String username;
    @NotBlank
    private String password;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "user")
    private Set<Task> tasks;

    @JsonIgnore
    private String firstnameLowercase;
    @JsonIgnore
    private String lastnameLowercase;

    // required for JPA
    public User() {}

    public User(String firstname, String lastname, String username, String password, Role role) {
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
        this.firstnameLowercase = firstname.toLowerCase();
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
        this.lastnameLowercase = lastname.toLowerCase();
    }
}