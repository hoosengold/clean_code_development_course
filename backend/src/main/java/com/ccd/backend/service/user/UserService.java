package com.ccd.backend.service.user;

import com.ccd.backend.entity.ApplicationUser;

public interface UserService {
    /**
     * Find an application user based on the email address.
     *
     * @param username username of the user
     * @return an application user
     */
    ApplicationUser findUserByUsername(String username);

    /**
     * Creates a user.
     *
     * @param applicationUser user to be created
     */
    void addUser(ApplicationUser applicationUser);

    /**
     * Finds user by given id.
     *
     * @param id id of the searched user
     * @return user with the given id
     */
    ApplicationUser findUserById(Long id);

    /**
     * @param applicationUser user
     * @param field           field to be updated
     * @param newValue        new value of the field
     */

    void updateUser(ApplicationUser applicationUser, String field, String newValue);

    /**
     * @param applicationUser user
     * @return ApplicationUser
     */
    void registerUser(ApplicationUser applicationUser);

    /**
     * @param applicationUser user
     * @return True if successful
     */
    Boolean loginUser(ApplicationUser applicationUser);

    /**
     * @param applicationUser user
     * @return User's score
     */
    int getUserScore(ApplicationUser applicationUser);

    /**
     * @param applicationUser user
     * @return ApplicationUser
     */
    ApplicationUser getUserDetails(ApplicationUser applicationUser);
}
