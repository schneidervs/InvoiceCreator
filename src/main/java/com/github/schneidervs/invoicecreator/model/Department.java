package com.github.schneidervs.invoicecreator.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "department")
    private List<UserData> users;

    public Department(String departmentName) {
        this.name = departmentName;
    }

    public Department() {
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
