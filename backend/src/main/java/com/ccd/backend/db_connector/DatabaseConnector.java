package com.ccd.backend.db_connector;

import com.ccd.backend.entity.ApplicationUser;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.Setter;

import java.sql.*;

public class DatabaseConnector {
    private final String jdbcUrl;
    private final String dbUser;
    private final String dbPassword;
    @Setter
    @Getter
    private Connection connection;

    public DatabaseConnector() {
        Dotenv dotenv = Dotenv.load();
        this.jdbcUrl = dotenv.get("JDBC_URL");
        this.dbUser = dotenv.get("DB_USER");
        this.dbPassword = dotenv.get("DB_PASSWORD");
    }

    public void openConnection() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(this.jdbcUrl, this.dbUser, this.dbPassword);
            System.out.println("Connected to the database.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.err.println("Error while closing the connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public ApplicationUser selectUser(long id) {
        String query = "SELECT id, email, password, score FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                int score = resultSet.getInt("score");
                System.out.println("Username: " + username + ", Email: " + email);
                return new ApplicationUser(id, username, email, password, score);
            } else {
                System.out.println("No user found with id " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error executing SELECT query: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public ApplicationUser selectUser(String username) {
        String query = "SELECT id, email, password, score FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                int score = resultSet.getInt("score");
                System.out.println("Username: " + username + ", Email: " + email);
                return new ApplicationUser(id, username, email, password, score);
            } else {
                System.out.println("No user found with username " + username);
            }
        } catch (SQLException e) {
            System.err.println("Error executing SELECT query: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void insertUser(ApplicationUser applicationUser) {
        String query = "INSERT INTO users (username, email, password, roleid, score) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, applicationUser.getUsername());
            statement.setString(2, applicationUser.getEmail());
            statement.setString(3, applicationUser.getPassword());
            statement.setString(4, applicationUser.getRole());
            statement.setInt(5, applicationUser.getScore());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User inserted successfully.");
            } else {
                System.out.println("Failed to insert user.");
            }
        } catch (SQLException e) {
            System.err.println("Error executing INSERT query: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateUser(long id, String field, String value) {
        String query = "UPDATE users SET " + field + " = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, value);
            statement.setLong(2, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("No user found with id " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error executing UPDATE query: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
