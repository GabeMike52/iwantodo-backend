package com.iwantodo.entities.user;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
//The class should have a no-arg constructor, will look into that later!
public class User {
    @Id
    @Column(name = "username", unique = true)
    private String username;
    private String email;
    private String password;

    public User() {

    }

    public User(String username, String email, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
