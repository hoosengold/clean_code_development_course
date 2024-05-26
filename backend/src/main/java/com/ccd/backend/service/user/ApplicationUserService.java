package com.ccd.backend.service.user;

import com.ccd.backend.db_connector.DatabaseConnector;
import com.ccd.backend.entity.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUserService implements UserService {
    private DatabaseConnector DbConnector;

    @Override
    public ApplicationUser findUserByUsername(String username) {
        this.DbConnector.openConnection();
        ApplicationUser applicationUser = this.DbConnector.selectUser(username);
        this.DbConnector.closeConnection();
        return applicationUser;
    }

    @Override
    public ApplicationUser findUserById(Long id) {
        this.DbConnector.openConnection();
        ApplicationUser applicationUser = this.DbConnector.selectUser(id);
        this.DbConnector.closeConnection();
        return applicationUser;
    }

    @Override
    public void addUser(ApplicationUser applicationUser) {
        this.DbConnector.openConnection();
        this.DbConnector.insertUser(applicationUser);
        this.DbConnector.closeConnection();
    }

    @Override
    public void updateUser(ApplicationUser applicationUser, String field, String newValue) {
        this.DbConnector.openConnection();
        this.DbConnector.updateUser(applicationUser.getId(), field, newValue);
        this.DbConnector.closeConnection();
    }

    @Override
    public void registerUser(ApplicationUser applicationUser) {
        this.addUser(applicationUser);
    }

    @Override
    public Boolean loginUser(ApplicationUser applicationUser) {
        this.DbConnector.openConnection();
        String password = this.DbConnector.selectUser(applicationUser.getUsername()).getPassword();
        this.DbConnector.closeConnection();
        return Objects.equals(password, applicationUser.getPassword());
    }

    @Override
    public int getUserScore(ApplicationUser applicationUser) {
        this.DbConnector.openConnection();
        int score = this.DbConnector.selectUser(applicationUser.getUsername()).getScore();
        this.DbConnector.closeConnection();
        return score;
    }

    @Override
    public ApplicationUser getUserDetails(ApplicationUser applicationUser) {
        this.DbConnector.openConnection();
        ApplicationUser NewApplicationUser = this.DbConnector.selectUser(applicationUser.getUsername());
        this.DbConnector.closeConnection();
        return NewApplicationUser;
    }
}
