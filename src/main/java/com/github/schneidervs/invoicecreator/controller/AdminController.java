package com.github.schneidervs.invoicecreator.controller;

import com.github.schneidervs.invoicecreator.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
