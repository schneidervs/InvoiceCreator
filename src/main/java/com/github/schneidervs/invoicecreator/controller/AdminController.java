package com.github.schneidervs.invoicecreator.controller;

import com.github.schneidervs.invoicecreator.model.*;
import com.github.schneidervs.invoicecreator.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService; // ваш сервис пользователей

    public AdminController(UserService userService) {
        this.userService = userService;
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
    public String saveUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/admin/users";
    }
}
