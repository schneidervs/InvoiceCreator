package com.github.schneidervs.invoicecreator.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "positions")
public class Position implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "position")
    private List<UserData> users;

    public Position() {
    }

    public Position(String positionName) {
        this.name = positionName;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserData> getUsers() {
        return users;
    }

    public void setUsers(List<UserData> users) {
        this.users = users;
    }
}
