package com.ccd.backend.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationUser {

    @Getter
    @Setter
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String email;

    private String password;

    @Getter
    @Setter
    private String role;

    @Getter
    @Setter
    private int score;

    @Setter
    @Getter
    @ElementCollection
    private List<String> roles;

    public ApplicationUser(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public ApplicationUser(Long id, String username, String email, String password, int score) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.score = score;
    }
}