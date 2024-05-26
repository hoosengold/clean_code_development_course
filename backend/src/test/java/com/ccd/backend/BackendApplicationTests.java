package com.ccd.backend;

import com.ccd.backend.db_connector.DatabaseConnector;
import com.ccd.backend.entity.ApplicationUser;
import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BackendApplicationTests {

    private ByteArrayOutputStream outputStream;
    private DatabaseConnector databaseConnector;
    private Connection mockConnection;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Initialize the DatabaseConnector with a mocked connection
        mockConnection = mock(Connection.class);
        databaseConnector = new DatabaseConnector();
        databaseConnector.setConnection(mockConnection);
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
    }

    @Test
    void when_openConnection_thenConnectionIsNotNull() {
        databaseConnector.openConnection();
        assertNotNull(databaseConnector.getConnection());
        databaseConnector.closeConnection();
    }

    @Test
    void when_closeConnection_thenConnectionIsNull() throws SQLException {
        databaseConnector.openConnection();
        databaseConnector.closeConnection();
        assertNull(databaseConnector.getConnection(), "Expected connection to be null after closing connection.");
    }

    @Test
    void when_closeConnectionTwice_thenConnectionIsNull() {
        databaseConnector.openConnection();
        databaseConnector.closeConnection();
        databaseConnector.closeConnection();
        assertNull(databaseConnector.getConnection(), "Expected connection to be null after closing connection twice.");
    }


    @Test
    void when_insertUser_thenUserIsInserted() throws SQLException {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Ensure that applicationUser matches the constructor you mentioned
        ApplicationUser applicationUser = new ApplicationUser(1L, "testuser", "test@example.com", "password", 100);

        databaseConnector.insertUser(applicationUser);

        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void when_selectUserById_thenReturnUser() throws SQLException {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("id")).thenReturn(1L);
        when(mockResultSet.getString("username")).thenReturn("testuser");
        when(mockResultSet.getString("email")).thenReturn("test@example.com");
        when(mockResultSet.getString("password")).thenReturn("password");
        when(mockResultSet.getInt("score")).thenReturn(100);

        ApplicationUser user = databaseConnector.selectUser(1L);
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(100, user.getScore());
    }

    @Test
    void when_selectUserByUsername_thenReturnUser() throws SQLException {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("id")).thenReturn(1L);
        when(mockResultSet.getString("username")).thenReturn("testuser");
        when(mockResultSet.getString("email")).thenReturn("test@example.com");
        when(mockResultSet.getString("password")).thenReturn("password");
        when(mockResultSet.getInt("score")).thenReturn(100);

        ApplicationUser user = databaseConnector.selectUser("testuser");
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(100, user.getScore());
    }

    @Test
    void when_updateUser_thenUserIsUpdated() throws SQLException {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        databaseConnector.updateUser(1L, "username", "newuser");

        verify(mockPreparedStatement, times(1)).executeUpdate();
    }
}
