package com.ccd.backend;

import com.ccd.backend.db_connector.DatabaseConnector;
import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BackendApplicationTests {

    private DatabaseConnector databaseConnector;
    private Connection mockConnection;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        // Initialize DatabaseConnector, mock connection, and capture console output
        databaseConnector = new DatabaseConnector();
        mockConnection = mock(Connection.class);
        databaseConnector.setConnection(mockConnection);

        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        // Close DatabaseConnector and restore System.out
        databaseConnector.closeConnection();
        System.setOut(System.out);
    }

    @Test
    void when_openConnection_thenConnectionIsNotNull() {
        // Test that opening connection initializes the connection
        databaseConnector.openConnection();
        assertNotNull(databaseConnector.getConnection());
    }

    @Test
    void when_closeConnection_thenConnectionIsNull() throws SQLException {
        // Test that closing connection sets connection to null
        databaseConnector.closeConnection();
        verify(mockConnection).close();
        assertNull(databaseConnector.getConnection());
    }

    @Test
    void when_openConnectionTwice_thenReuseExistingConnection() {
        // Test that opening connection twice reuses the existing connection
        databaseConnector.openConnection();
        Connection existingConnection = databaseConnector.getConnection();
        databaseConnector.openConnection();
        assertEquals(existingConnection, databaseConnector.getConnection());
    }

    @Test
    void when_insertUserWithInvalidValues_thenThrowSQLException() throws SQLException {
        // Test that inserting user with invalid values throws SQLException
        doThrow(new SQLException("Invalid values")).when(mockConnection).prepareStatement(anyString());

        SQLException exception = assertThrows(SQLException.class, () -> databaseConnector.insertUser(null, null, null, null, 0));
        assertTrue(exception.getMessage().contains("Invalid values"));
    }

    @Test
    void when_openConnectionFails_thenConnectionIsNull() {
        // Test that opening connection with invalid credentials results in null connection
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.openConnection();
        assertNull(databaseConnector.getConnection());
    }

    @Test
    void when_SQLExceptionDuringOperation_thenPrintErrorMessage() throws SQLException {
        // Test that SQLException during operation prints error message
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(new SQLException("Query execution failed"));

        SQLException exception = assertThrows(SQLException.class, () -> databaseConnector.selectUser(1));
        assertTrue(exception.getMessage().contains("Query execution failed"));
        assertTrue(outputStream.toString().contains("Error executing SELECT query"));
    }

    @Test
    void when_selectUser_thenPrintUserInfo() throws SQLException {
        // Test that selectUser method prints user info
        int id = 1;
        String username = "testuser";
        String email = "testuser@example.com";
        String password = "password123";

        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("username")).thenReturn(username);
        when(mockResultSet.getString("email")).thenReturn(email);
        when(mockResultSet.getString("password")).thenReturn(password);

        databaseConnector.selectUser(id);

        String expectedOutput = "Username: " + username + ", Email: " + email + ", Password: " + password + "\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void when_insertUser_thenPrintSuccessMessage() throws SQLException {
        // Test that insertUser method prints success message
        String username = "newuser";
        String email = "newuser@example.com";
        String password = "newpassword";
        String role = "admin";
        int initialScore = 100;

        PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        databaseConnector.insertUser(username, email, password, role, initialScore);

        String expectedQuery = "INSERT INTO users (username, email, password, role, initial_score) VALUES (?, ?, ?, ?, ?)";
        verify(mockConnection).prepareStatement(expectedQuery);
        verify(mockStatement).setString(1, username);
        verify(mockStatement).setString(2, email);
        verify(mockStatement).setString(3, password);
        verify(mockStatement).setString(4, role);
        verify(mockStatement).setInt(5, initialScore);

        String expectedOutput = "User inserted successfully.\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void when_updateUser_thenPrintSuccessMessage() throws SQLException {
        // Test that updateUser method prints success message
        int id = 1;
        String field = "username";
        String value = "updateduser";

        PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        databaseConnector.updateUser(id, field, value);

        String expectedQuery = "UPDATE users SET " + field + " = ? WHERE id = ?";
        verify(mockConnection).prepareStatement(expectedQuery);
        verify(mockStatement).setString(1, value);
        verify(mockStatement).setInt(2, id);

        String expectedOutput = "User updated successfully.\n";
        assertEquals(expectedOutput, outputStream.toString());
    }
}
