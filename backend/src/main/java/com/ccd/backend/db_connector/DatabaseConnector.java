package com.ccd.backend.db_connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
}