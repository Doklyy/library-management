package com.library.entity;
import com.library.utlis.TimeUtlis;
import com.mysql.cj.util.TimeUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(nullable = false, unique = true)
    private  String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "full_name")
    private String fullName;
    private String phone;
    @Column(nullable = false)
    private boolean active = true;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    @Column
    private String fullname;
    @Column
    private String address;
    @Column
    private Timestamp dob;
    @Column(updatable = false)
    private String identityNumber;
    @Column(updatable = false)
    private Timestamp createdAt;
    @Column
    private Timestamp updatedAt;
    @Column
    private Boolean isDeleted;
    @PrePersist
    protected void onCreate() {
        this.isDeleted = false;
        this.createdAt = TimeUtlis.getCurrentTimestamp();
        this.updatedAt = null;
    }
    @PreUpdate
    protected void upDate(){
        this.updatedAt= TimeUtlis.getCurrentTimestamp();
    }
}
