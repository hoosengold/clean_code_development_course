package com.ccd.backend;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    // This method should interact with your data store to verify user credentials.
    // For simplicity, this example uses hardcoded values.
    public boolean verifyUser(String username, String password) {
        // Replace this logic with actual user verification logic (e.g., database query).
        return "testuser".equals(username) && "password".equals(password);
    }
}
