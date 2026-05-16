package com.nidhi.finflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //primary key, auto incremented

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; //USER ADMIN

    private LocalDateTime createdAt;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Expense> expenses;
    //automatically adds creation time
    @PrePersist
    public  void prePersist(){
        this.createdAt = LocalDateTime.now();
    }
}
