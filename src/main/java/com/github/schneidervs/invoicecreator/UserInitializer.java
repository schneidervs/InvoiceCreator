package com.github.schneidervs.invoicecreator;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger =LoggerFactory.getLogger(UserInitializer.class);

    public UserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initUsers() {
        createUserIfNotExists("admin", "admin123", Set.of("ROLE_ADMIN"));
        createUserIfNotExists("user", "user123", Set.of("ROLE_USER"));
    }

    private void createUserIfNotExists(String username, String rawPassword, Set<String> roles) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setRoles(roles);
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            userRepository.save(user);
            logger.info("Created user: {}", username);
        } else {
            logger.info("User '{}' already exists — skipping.", username);
        }
    }
}

