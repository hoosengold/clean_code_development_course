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

    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        // Capture console output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        // Restore System.out
        System.setOut(System.out);
    }

    @Test
    void when_openConnection_thenConnectionIsNotNull() {
        // Initialize DatabaseConnector and test that opening connection initializes the connection
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.openConnection();
        assertNotNull(databaseConnector.getConnection());
        databaseConnector.closeConnection();
    }

    @Test
    void when_closeConnection_thenConnectionIsNull() throws SQLException {
        // Initialize DatabaseConnector and test that closing connection sets connection to null
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.openConnection();  // Ensure connection is opened first
        databaseConnector.closeConnection();
        assertNull(databaseConnector.getConnection(), "Expected connection to be null after closing");
    }

    @Test
    void when_openConnectionTwice_thenReuseExistingConnection() {
        // Initialize DatabaseConnector and test that opening connection twice reuses the existing connection
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.openConnection();
        Connection existingConnection = databaseConnector.getConnection();
        databaseConnector.openConnection();
        assertEquals(existingConnection, databaseConnector.getConnection());
        databaseConnector.closeConnection();
    }

    @Test
    void when_insertUserWithInvalidValues_thenThrowSQLException() {
        // Initialize DatabaseConnector and test that inserting user with invalid values throws SQLException
        DatabaseConnector databaseConnector = new DatabaseConnector();
        SQLException exception = assertThrows(SQLException.class, () ->
            databaseConnector.insertUser(null, null, null, null, 0)
        );
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
        DatabaseConnector databaseConnector = new DatabaseConnector();
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        doThrow(new SQLException("Query execution failed")).when(mockStatement).executeQuery();

        databaseConnector.setConnection(mockConnection);
        SQLException exception = assertThrows(SQLException.class, () -> databaseConnector.selectUser(1));
        assertEquals("Query execution failed", exception.getMessage());
    }


    @Test
    void when_selectUser_thenPrintUserInfo() throws SQLException {
        // Test that selectUser method prints user info
        DatabaseConnector databaseConnector = new DatabaseConnector();
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("username")).thenReturn("testuser");
        when(mockResultSet.getString("email")).thenReturn("testuser@example.com");
        when(mockResultSet.getString("password")).thenReturn("password123");

        databaseConnector.setConnection(mockConnection);

        // Capture the output of selectUser method
        String actualOutput = databaseConnector.selectUser(1);

        String expectedOutput = "Username: testuser, Email: testuser@example.com, Password: password123";
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void when_insertUser_thenPrintSuccessMessage() throws SQLException {
        // Test that insertUser method prints success message
        DatabaseConnector databaseConnector = new DatabaseConnector();
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        databaseConnector.setConnection(mockConnection);
        databaseConnector.insertUser("newuser", "newuser@example.com", "newpassword", "admin", 100);

        String expectedOutput = "User inserted successfully.\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void when_updateUser_thenPrintSuccessMessage() throws SQLException {
        // Test that updateUser method prints success message
        DatabaseConnector databaseConnector = new DatabaseConnector();
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        databaseConnector.setConnection(mockConnection);
        databaseConnector.updateUser(1, "username", "updateduser");

        String expectedOutput = "User updated successfully.\n";
        assertEquals(expectedOutput, outputStream.toString());
    }
}


