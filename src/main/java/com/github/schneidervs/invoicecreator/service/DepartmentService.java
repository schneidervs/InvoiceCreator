package com.github.schneidervs.invoicecreator.service;

import com.github.schneidervs.invoicecreator.model.Department;
import com.github.schneidervs.invoicecreator.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    public void create(Department department) {
        departmentRepository.save(department);
    }

    public void update(Department department) {
        departmentRepository.save(department);
    }

    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }

    public boolean departmentExists(String name) {
        return departmentRepository.findByName(name).isPresent();
    }
}
