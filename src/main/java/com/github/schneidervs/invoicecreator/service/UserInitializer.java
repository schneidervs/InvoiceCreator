package com.github.schneidervs.invoicecreator.service;

import com.github.schneidervs.invoicecreator.model.*;
import com.github.schneidervs.invoicecreator.repository.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.*;

import java.util.Set;

@Component
public class UserInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final Logger logger = LoggerFactory.getLogger(UserInitializer.class);

    public UserInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           PositionRepository positionRepository,
                           DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
    }

    @PostConstruct
    public void initUsers() {
        createUserIfNotExists(
                "admin", "admin123", Set.of("ROLE_ADMIN"),
                "Anna", "Nowak", "Manager", "IT");

        createUserIfNotExists(
                "user", "user123", Set.of("ROLE_USER"),
                "Jan", "Kowalski", "Accountant", "Finance");

        createUserIfNotExists(
                "staff", "staff123", Set.of("ROLE_STAFF"),
                "Maria", "Zielińska", "Assistant", "HR");
    }

    private void createUserIfNotExists(String username,
                                       String rawPassword,
                                       Set<String> roles,
                                       String firstName,
                                       String lastName,
                                       String positionName,
                                       String departmentName) {
        if (userRepository.findByUsername(username).isPresent()) {
            logger.info("User '{}' already exists — skipping.", username);
            return;
        }

        // Найти или создать должность
        Position position = positionRepository.findByName(positionName)
                .orElseGet(() -> positionRepository.save(new Position(positionName)));

        // Найти или создать отдел
        Department department = departmentRepository.findByName(departmentName)
                .orElseGet(() -> departmentRepository.save(new Department(departmentName)));

        // Создать user и userData
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoles(roles);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        UserData userData = new UserData();
        userData.setFirstName(firstName);
        userData.setLastName(lastName);
        userData.setPosition(position);
        userData.setDepartment(department);
        userData.setUser(user);         // связь с User
        user.setUserData(userData);     // обратная связь

        userRepository.save(user);
        logger.info("Created user: {}", username);
    }
}
