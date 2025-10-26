package org.example.controllers;

import org.example.daos.UserDao;
import org.example.models.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Controller for the profile of the currently logged-in user.
 */
@RestController
@RequestMapping("/api/profile")
@PreAuthorize("isAuthenticated()")
public class ProfileController {

    private final UserDao userDao;

    public ProfileController(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Gets the profile of the currently logged-in user.
     */
    @GetMapping
    public User getProfile(Principal principal) {
        return userDao.getByUsername(principal.getName());
    }

    /**
     * Gets the roles of the currently logged-in user.
     */
    @GetMapping("/roles")
    public List<String> getRoles(Principal principal) {
        return userDao.getRoles(principal.getName());
    }

    /**
     * Changes the password of the currently logged-in user.
     */
    @PutMapping("/change-password")
    public User changePassword(Principal principal, @RequestBody String newPassword) {
        boolean success = userDao.updatePassword(principal.getName(), newPassword);
        if (!success) {
            throw new RuntimeException("Password update failed");
        }
        return userDao.getByUsername(principal.getName());
    }
}
