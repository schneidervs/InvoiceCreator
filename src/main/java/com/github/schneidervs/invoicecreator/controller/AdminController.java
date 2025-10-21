package com.github.schneidervs.invoicecreator.controller;

import com.github.schneidervs.invoicecreator.model.*;
import com.github.schneidervs.invoicecreator.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final String REDIRECT_USERS = "redirect:/admin/users";
    private static final String ROLES = "roles";
    private static final String USERS = "users";

    private final UserService userService;
    private final PositionService positionService;
    private final DepartmentService departmentService;

    public AdminController(UserService userService, PositionService positionService, DepartmentService departmentService) {
        this.userService = userService;
        this.positionService = positionService;
        this.departmentService = departmentService;
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute(USERS, userService.findAll());
        return "admin/users";
    }

    @GetMapping("/users/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute(USERS, new User());
        model.addAttribute(ROLES, List.of(Role.values()));
        return "admin/new-user";
    }

    @PostMapping("/users/save")
    public String createUser(@ModelAttribute("user") User user,Model model) {
        if (userService.userExists(user.getUsername())) {
            model.addAttribute("errorMessage", "User with username " + user.getUsername() + " already exists.");
            model.addAttribute(USERS, user);
            model.addAttribute(ROLES, List.of(Role.values()));
            return "admin/new-user";
        }
        userService.create(user);
        return REDIRECT_USERS;
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return REDIRECT_USERS;
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute(USERS, user);
        model.addAttribute(ROLES, List.of(Role.values()));
        model.addAttribute("positions", positionService.findAll());
        model.addAttribute("departments", departmentService.findAll());
        return "admin/edit-user";
    }

    @PostMapping("/users/update")
    public String updateUser(
            @RequestParam(value = "id", required = false) Long id, //kostyl
            @ModelAttribute(USERS) User updatedUser) {

        User existingUser = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + updatedUser.getId()));
        existingUser.setRoles(updatedUser.getRoles());
        existingUser.setEnabled(updatedUser.isEnabled());
        existingUser.setAccountNonExpired(updatedUser.isAccountNonExpired());
        existingUser.setAccountNonLocked(updatedUser.isAccountNonLocked());
        existingUser.setCredentialsNonExpired(updatedUser.isCredentialsNonExpired());

        userService.update(existingUser);
        return REDIRECT_USERS;
    }
}
