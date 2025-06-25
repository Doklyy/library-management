package com.library.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String roleName;
    
    private String permissions;
    private int priority;
    private boolean isActive;
    private boolean isDeleted;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<User> users;

    public enum RoleType {
        ADMIN,
        LIBRARIAN,
        USER
    }
    
    public String name() {
        return this.roleName;
    }
}
