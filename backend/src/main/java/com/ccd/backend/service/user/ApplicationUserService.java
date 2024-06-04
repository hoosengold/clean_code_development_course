package com.ccd.backend.service.user;

import com.ccd.backend.db_connector.DatabaseConnector;
import com.ccd.backend.entity.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUserService implements UserService {
    private DatabaseConnector DbConnector = new DatabaseConnector();
    private static final Set<String> fieldOptions = Set.of("username", "email", "password", "score");

    @Override
    public ApplicationUser findUserByUsername(String username) {
        if (Objects.equals(username, "")) {
            throw new IllegalArgumentException("username cannot be empty");
        }
        ApplicationUser applicationUser;
        try {
            this.DbConnector.openConnection();
            applicationUser = this.DbConnector.selectUser(username);
        } finally {
            this.DbConnector.closeConnection();
        }
        if (applicationUser == null) {
            throw new RuntimeException("User not found");
        }
        return applicationUser;
    }

    @Override
    public ApplicationUser findUserById(Long id) {
        if (id < 0) {
            throw new IllegalArgumentException("id cannot be negative");
        }
        ApplicationUser applicationUser;
        try {
            this.DbConnector.openConnection();
            applicationUser = this.DbConnector.selectUser(id);
        } finally {
            this.DbConnector.closeConnection();
        }
        if (applicationUser == null) {
            throw new RuntimeException("User not found");
        }
        return applicationUser;
    }

    @Override
    public void addUser(ApplicationUser applicationUser) {
        if (applicationUser == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        try {
            this.DbConnector.openConnection();
            this.DbConnector.insertUser(applicationUser);
        } finally {
            this.DbConnector.closeConnection();
        }
    }

    @Override
    public void updateUser(ApplicationUser applicationUser, String field, String newValue) {
        if (applicationUser == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (!fieldOptions.contains(field)) {
            throw new IllegalArgumentException("Field can only be one of username, password, email or score");
        }
        if (Objects.equals(newValue, "")) {
            throw new IllegalArgumentException("newValue cannot be empty");
        }

        try {
            this.DbConnector.openConnection();
            this.DbConnector.updateUser(applicationUser.getId(), field, newValue);
        } finally {
            this.DbConnector.closeConnection();
        }
    }

    @Override
    public void updateUser(long id, String field, String newValue) {
        if (id < 0) {
            throw new IllegalArgumentException("id cannot be negative");
        }
        if (!Objects.equals(field, "") && !fieldOptions.contains(field)) {
            throw new IllegalArgumentException("Field can only be one of username, password, email or score");
        }
        if (Objects.equals(newValue, "")) {
            throw new IllegalArgumentException("newValue cannot be empty");
        }

        try {
            this.DbConnector.openConnection();
            this.DbConnector.updateUser(id, field, newValue);
        } finally {
            this.DbConnector.closeConnection();
        }
    }

    @Override
    public void registerUser(ApplicationUser applicationUser) {
        if (applicationUser == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.addUser(applicationUser);
    }

    @Override
    public Boolean loginUser(ApplicationUser applicationUser) {
        if (applicationUser == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        String password;
        try {
            this.DbConnector.openConnection();
            ApplicationUser userFromDb = this.DbConnector.selectUser(applicationUser.getUsername());
            if (userFromDb == null) {
                throw new RuntimeException("User not found");
            }
            password = userFromDb.getPassword();
        } finally {
            this.DbConnector.closeConnection();
        }
        return Objects.equals(password, applicationUser.getPassword());
    }

    @Override
    public int getUserScore(ApplicationUser applicationUser) {
        if (applicationUser == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        int score;
        try {
            this.DbConnector.openConnection();
            ApplicationUser userFromDb = this.DbConnector.selectUser(applicationUser.getUsername());
            if (userFromDb == null) {
                throw new RuntimeException("User not found");
            }
            score = userFromDb.getScore();
        } finally {
            this.DbConnector.closeConnection();
        }
        return score;
    }

    @Override
    public ApplicationUser getUserDetails(ApplicationUser applicationUser) {
        if (applicationUser == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        ApplicationUser NewApplicationUser;
        try {
            this.DbConnector.openConnection();
            NewApplicationUser = this.DbConnector.selectUser(applicationUser.getUsername());
            if (NewApplicationUser == null) {
                throw new RuntimeException("User not found");
            }
        } finally {
            this.DbConnector.closeConnection();
        }
        return NewApplicationUser;
    }
}

