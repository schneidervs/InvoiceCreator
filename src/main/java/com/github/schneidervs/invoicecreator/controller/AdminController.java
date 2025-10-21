package com.github.schneidervs.invoicecreator.controller;

import com.github.schneidervs.invoicecreator.model.*;
import com.github.schneidervs.invoicecreator.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final String REDIRECT_USERS = "redirect:/admin/users";
    private static final String ROLES = "roles";
    private static final String USERS = "users";
    private static final String USER = "user";
    private static final String POSITIONS = "positions";
    private static final String DEPARTMENTS = "departments";

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
        User user = new User();
        UserData userData = new UserData();
        userData.setUser(user);
        user.setUserData(userData);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setRoles(Set.of(Role.ROLE_USER.name()));

        model.addAttribute(USER, user);
        model.addAttribute(ROLES, List.of(Role.values()));
        model.addAttribute(POSITIONS, positionService.findAll());
        model.addAttribute(DEPARTMENTS, departmentService.findAll());
        return "admin/new-user";
    }

    @PostMapping("/users/save")
    public String createUser(
            @RequestParam(value = "positionId", required = false) Long positionId,
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @ModelAttribute("user") User user,
            Model model) {

        if (userService.userExists(user.getUsername())) {
            prepareUserForm(model, user, "User with username " + user.getUsername() + " already exists.");
            return "admin/new-user";
        }

        userService.create(user);

        if (isUserDataProvided(firstName, lastName, positionId, departmentId)) {
            UserData userData = Optional.ofNullable(user.getUserData()).orElse(new UserData());
            userData.setUser(user);
            userData.setFirstName(firstName);
            userData.setLastName(lastName);
            userData.setPosition(positionService.findById(positionId).orElse(null));
            userData.setDepartment(departmentService.findById(departmentId).orElse(null));

            user.setUserData(userData);
            userService.update(user);
        }

        return REDIRECT_USERS;
    }

    private boolean isUserDataProvided(String firstName, String lastName, Long positionId, Long departmentId) {
        return firstName != null || lastName != null || positionId != null || departmentId != null;
    }

    private void prepareUserForm(Model model, User user, String errorMessage) {
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute(USER, user);
        model.addAttribute(ROLES, List.of(Role.values()));
        model.addAttribute(POSITIONS, positionService.findAll());
        model.addAttribute(DEPARTMENTS, departmentService.findAll());
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        if (user.getUserData() == null) {
            UserData userData = new UserData();
            userData.setUser(user);
            user.setUserData(userData);
            userService.update(user);
        }

        model.addAttribute(USER, user);
        model.addAttribute(ROLES, List.of(Role.values()));
        model.addAttribute(POSITIONS, positionService.findAll());
        model.addAttribute(DEPARTMENTS, departmentService.findAll());
        return "admin/edit-user";
    }

    @PostMapping("/users/update")
    @Transactional
    public String updateUser(
            @RequestParam("id") Long id,
            @RequestParam(value = "positionId", required = false) Long positionId,
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @ModelAttribute(USER) User updatedUser) {

        User existingUser = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        existingUser.setRoles(updatedUser.getRoles());
        existingUser.setEnabled(updatedUser.isEnabled());
        existingUser.setAccountNonExpired(updatedUser.isAccountNonExpired());
        existingUser.setAccountNonLocked(updatedUser.isAccountNonLocked());
        existingUser.setCredentialsNonExpired(updatedUser.isCredentialsNonExpired());

        UserData ud = existingUser.getUserData();
        if (ud == null) {
            ud = new UserData();
            ud.setUser(existingUser);
            existingUser.setUserData(ud);
        }

        ud.setFirstName(firstName);
        ud.setLastName(lastName);

        if (positionId != null) {
            Optional<Position> positionOpt = positionService.findById(positionId);
            if (positionOpt.isPresent()) {
                Position position = positionOpt.get();

                ud.setPosition(position);
            } else {

                ud.setPosition(null);
            }
        } else {
            ud.setPosition(null);
        }

        if (departmentId != null) {
            Optional<Department> departmentOpt = departmentService.findById(departmentId);
            if (departmentOpt.isPresent()) {
                Department department = departmentOpt.get();
                ud.setDepartment(department);
            } else {
                ud.setDepartment(null);
            }
        } else {
            ud.setDepartment(null);
        }
        userService.update(existingUser);
        return REDIRECT_USERS;
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return REDIRECT_USERS;
    }
}
