package com.github.schneidervs.invoicecreator.repository;

import com.github.schneidervs.invoicecreator.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);
}
