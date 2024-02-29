package com.ccd.backend.db_connector;

import java.sql.*;

public class DatabaseConnector {
    private final String jdbcUrl;
    private final String dbUser;
    private final String dbPassword;
    private Connection connection;

    public DatabaseConnector() {
        this.jdbcUrl = System.getenv("JDBC_URL");
        this.dbUser = System.getenv("DB_USER");
        this.dbPassword = System.getenv("DB_PASSWORD");
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
    public void selectUserById(int id) {
        String sql = "SELECT username, email, password FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                System.out.println("Username: " + username + ", Email: " + email + ", Password: " + password);
            } else {
                System.out.println("User with id " + id + " not found.");
            }
        } catch (SQLException e) {
            System.err.println("Error executing SELECT query: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void insertUser(String username, String email, String password) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User inserted successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error executing INSERT query: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateUser(int id, String username, String email, String password) {
        String sql = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setInt(4, id);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("User with id " + id + " not found.");
            }
        } catch (SQLException e) {
            System.err.println("Error executing UPDATE query: " + e.getMessage());
            e.printStackTrace();
        }
    }
}