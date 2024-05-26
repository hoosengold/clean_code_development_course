package com.ccd.backend.RestAPI;

import com.ccd.backend.entity.ApplicationUser;
import com.ccd.backend.service.user.ApplicationUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api/test")
public class UserController {


    @CrossOrigin
    @GetMapping("/register")
    public ResponseEntity<String> registerUser(
        @RequestParam String username,
        @RequestParam String email,
        @RequestParam String password) {


        if (username == null || email == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing parameters");
        }

        ApplicationUserService userService = new ApplicationUserService();
        ApplicationUser user = new ApplicationUser(username, email, password, 0);
        try {
            userService.registerUser(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong while registereing the user! Please try again!");
        }
        return ResponseEntity.status(HttpStatus.OK).body("User registered successfully");
    }
}