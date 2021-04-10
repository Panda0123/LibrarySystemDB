package com.library.database_system.domain;

import javax.persistence.*;

@Entity
@Table
public class Admin {

    @Id
    @SequenceGenerator(
            name = "sequence_admin",
            sequenceName = "sequence_admin",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_admin"
    )
    private Long id;

    private String username;
    private String password;

    public Admin() {
    }

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
