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
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/users/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", List.of(Role.values()));
        return "admin/new-user";
    }

    @PostMapping("/users/save")
    public String createUser(@ModelAttribute("user") User user) {
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
        model.addAttribute("user", user);
        model.addAttribute("roles", List.of(Role.values()));
        model.addAttribute("positions", positionService.findAll());
        model.addAttribute("departments", departmentService.findAll());
        return "admin/edit-user";
    }

    @PostMapping("/users/update")
    public String updateUser(
            @RequestParam(value = "id", required = false) Long id, //kostyl
            @ModelAttribute("user") User updatedUser) {

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
