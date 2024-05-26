package com.ccd.backend.db_connector;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

public class DatabaseConnector {
    private final String jdbcUrl;
    private final String dbUser;
    private final String dbPassword;
    private Connection connection;

    public DatabaseConnector() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
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
            connection = null; // Set connection to null if connection fails
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
            } finally {
                connection = null; // Set connection to null
            }
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public String selectUser(int id) throws SQLException {
        String query = "SELECT username, email, password FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                return "Username: " + username + ", Email: " + email + ", Password: " + password;
            } else {
                return "No user found with id " + id;
            }
        } catch (SQLException e) {
            System.err.println("Error executing SELECT query: " + e.getMessage());
            throw e; // Rethrow the exception
        }
    }

    public void insertUser(String username, String email, String password, String role, int initialScore) throws SQLException {
        if (username == null || email == null || password == null || role == null) {
            throw new SQLException("Invalid values");
        }
        String query = "INSERT INTO users (username, email, password, role, initial_score) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, role);
            statement.setInt(5, initialScore);
            statement.executeUpdate();
            System.out.println("User inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error executing INSERT query: " + e.getMessage());
            throw e;
        }
    }


    public void updateUser(int id, String field, String value) {
        String query = "UPDATE users SET " + field + " = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, value);
            statement.setInt(2, id);
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
