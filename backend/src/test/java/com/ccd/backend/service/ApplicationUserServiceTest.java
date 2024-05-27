package com.ccd.backend.service;

import com.ccd.backend.db_connector.DatabaseConnector;
import com.ccd.backend.entity.ApplicationUser;
import com.ccd.backend.service.user.ApplicationUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationUserServiceTest {

    @Mock
    private DatabaseConnector dbConnector;

    @InjectMocks
    private ApplicationUserService applicationUserService;

    private List<ApplicationUser> mockDatabase;
    private ApplicationUser testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockDatabase = new ArrayList<>();

        testUser = new ApplicationUser();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setPassword("password");
        testUser.setScore(100);

        mockDatabase.add(testUser);
    }

    @AfterEach
    void tearDown() {
        mockDatabase.clear();
    }

    @Nested
    class FindUserByUsername {
        @Test
        void when_findUserByUsername_then_returnUser() {
            when(dbConnector.selectUser("testUser")).thenReturn(testUser);

            ApplicationUser user = applicationUserService.findUserByUsername("testUser");

            assertEquals(testUser, user);
            verify(dbConnector).openConnection();
            verify(dbConnector).selectUser("testUser");
            verify(dbConnector).closeConnection();
        }

        @Test
        void when_findUserByUsernameWithInvalidUsername_then_throwException() {
            when(dbConnector.selectUser("invalidUser")).thenReturn(null);

            assertThrows(RuntimeException.class, () -> {
                applicationUserService.findUserByUsername("invalidUser");
            });

            verify(dbConnector).openConnection();
            verify(dbConnector).selectUser("invalidUser");
            verify(dbConnector).closeConnection();
        }
    }

    @Nested
    class FindUserById {
        @Test
        void when_findUserById_then_returnUser() {
            when(dbConnector.selectUser(1L)).thenReturn(testUser);

            ApplicationUser user = applicationUserService.findUserById(1L);

            assertEquals(testUser, user);
            verify(dbConnector).openConnection();
            verify(dbConnector).selectUser(1L);
            verify(dbConnector).closeConnection();
        }

        @Test
        void when_findUserByIdWithInvalidId_then_throwException() {
            when(dbConnector.selectUser(999L)).thenReturn(null);

            assertThrows(RuntimeException.class, () -> {
                applicationUserService.findUserById(999L);
            });

            verify(dbConnector).openConnection();
            verify(dbConnector).selectUser(999L);
            verify(dbConnector).closeConnection();
        }
    }

    @Nested
    class AddUser {
        @Test
        void when_addUser_then_verifyDatabaseInsertion() {
            ApplicationUser newUser = new ApplicationUser();
            newUser.setId(2L);
            newUser.setUsername("newUser");
            newUser.setPassword("newPassword");

            doAnswer(invocation -> {
                ApplicationUser user = invocation.getArgument(0);
                mockDatabase.add(user);
                return null;
            }).when(dbConnector).insertUser(any(ApplicationUser.class));

            applicationUserService.addUser(newUser);

            assertTrue(mockDatabase.contains(newUser));
            verify(dbConnector).openConnection();
            verify(dbConnector).insertUser(newUser);
            verify(dbConnector).closeConnection();
        }

        @Test
        void when_addUserWithNullUser_then_throwException() {
            assertThrows(IllegalArgumentException.class, () -> {
                applicationUserService.addUser(null);
            });
            verify(dbConnector, never()).closeConnection();
        }
    }

    @Nested
    class UpdateUser {
        @Test
        void when_updateUser_then_verifyDatabaseUpdate() {
            doAnswer(invocation -> {
                Long id = invocation.getArgument(0);
                String field = invocation.getArgument(1);
                String newValue = invocation.getArgument(2);
                ApplicationUser user = mockDatabase.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
                if (user != null) {
                    switch (field) {
                        case "username":
                            user.setUsername(newValue);
                            break;
                        case "password":
                            user.setPassword(newValue);
                            break;
                        case "score":
                            user.setScore(Integer.parseInt(newValue));
                            break;
                        default:
                            throw new RuntimeException("Invalid field");
                    }
                }
                return null;
            }).when(dbConnector).updateUser(anyLong(), anyString(), anyString());

            applicationUserService.updateUser(testUser, "password", "newPassword");

            assertEquals("newPassword", testUser.getPassword());
            verify(dbConnector).openConnection();
            verify(dbConnector).updateUser(1L, "password", "newPassword");
            verify(dbConnector).closeConnection();
        }

        @Test
        void when_updateUserWithInvalidField_then_throwException() {
            assertThrows(IllegalArgumentException.class, () -> {
                applicationUserService.updateUser(1L, "invalidField", "newValue");
            });

            verify(dbConnector, never()).openConnection();
        }

        @Test
        void when_updateUserWithInvalidNewValue_then_throwException() {
            assertThrows(IllegalArgumentException.class, () -> {
                applicationUserService.updateUser(1L, "username", "");
            });

            verify(dbConnector, never()).openConnection();
        }

        @Test
        void when_updateUserWithNullUser_then_throwException() {
            assertThrows(IllegalArgumentException.class, () -> {
                applicationUserService.updateUser(null, "testUser", "newUsername");
            });
            verify(dbConnector, never()).openConnection();
        }
    }

    @Nested
    class RegisterUser {
        @Test
        void when_registerUser_then_addUser() {
            ApplicationUser newUser = new ApplicationUser();
            newUser.setId(2L);
            newUser.setUsername("newUser");
            newUser.setPassword("newPassword");

            doAnswer(invocation -> {
                ApplicationUser user = invocation.getArgument(0);
                mockDatabase.add(user);
                return null;
            }).when(dbConnector).insertUser(any(ApplicationUser.class));

            applicationUserService.registerUser(newUser);

            assertTrue(mockDatabase.contains(newUser));
            verify(dbConnector).openConnection();
            verify(dbConnector).insertUser(newUser);
            verify(dbConnector).closeConnection();
        }

        @Test
        void when_registerUserWithNullUser_then_throwException() {
            assertThrows(IllegalArgumentException.class, () -> {
                applicationUserService.registerUser(null);
            });
            verify(dbConnector, never()).closeConnection();
        }
    }

    @Nested
    class LoginUser {
        @Test
        void when_loginUser_then_returnTrueForValidPassword() {
            when(dbConnector.selectUser("testUser")).thenReturn(testUser);

            Boolean result = applicationUserService.loginUser(testUser);

            assertTrue(result);
            verify(dbConnector).openConnection();
            verify(dbConnector).selectUser("testUser");
            verify(dbConnector).closeConnection();
        }

        @Test
        void when_loginUser_then_returnFalseForInvalidPassword() {
            ApplicationUser invalidUser = new ApplicationUser();
            invalidUser.setUsername("testUser");
            invalidUser.setPassword("wrongPassword");

            when(dbConnector.selectUser("testUser")).thenReturn(testUser);

            Boolean result = applicationUserService.loginUser(invalidUser);

            assertFalse(result);
            verify(dbConnector).openConnection();
            verify(dbConnector).selectUser("testUser");
            verify(dbConnector).closeConnection();
        }

        @Test
        void when_loginUserWithInvalidUsername_then_throwException() {
            when(dbConnector.selectUser("invalidUser")).thenReturn(null);

            assertThrows(RuntimeException.class, () -> {
                applicationUserService.loginUser(new ApplicationUser("invalidUser", "password"));
            });

            verify(dbConnector).openConnection();
            verify(dbConnector).selectUser("invalidUser");
            verify(dbConnector).closeConnection();
        }
    }

    @Nested
    class GetUserScore {
        @Test
        void when_getUserScore_then_returnScore() {
            when(dbConnector.selectUser("testUser")).thenReturn(testUser);

            int score = applicationUserService.getUserScore(testUser);

            assertEquals(100, score);
            verify(dbConnector).openConnection();
            verify(dbConnector).selectUser("testUser");
            verify(dbConnector).closeConnection();
        }

        @Test
        void when_getUserScoreWithInvalidUsername_then_throwException() {
            when(dbConnector.selectUser("invalidUser")).thenReturn(null);

            assertThrows(RuntimeException.class, () -> {
                applicationUserService.getUserScore(new ApplicationUser("invalidUser", "password"));
            });

            verify(dbConnector).openConnection();
            verify(dbConnector).selectUser("invalidUser");
            verify(dbConnector).closeConnection();
        }
    }

    @Nested
    class GetUserDetails {
        @Test
        void when_getUserDetails_then_returnUserDetails() {
            when(dbConnector.selectUser("testUser")).thenReturn(testUser);

            ApplicationUser user = applicationUserService.getUserDetails(testUser);

            assertEquals(testUser, user);
            verify(dbConnector).openConnection();
            verify(dbConnector).selectUser("testUser");
            verify(dbConnector).closeConnection();
        }

        @Test
        void when_getUserDetailsWithInvalidUsername_then_throwException() {
            when(dbConnector.selectUser("invalidUser")).thenReturn(null);

            assertThrows(RuntimeException.class, () -> {
                applicationUserService.getUserDetails(new ApplicationUser("invalidUser", "password"));
            });

            verify(dbConnector).openConnection();
            verify(dbConnector).selectUser("invalidUser");
            verify(dbConnector).closeConnection();
        }
    }
}
