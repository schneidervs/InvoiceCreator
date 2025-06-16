package com.github.schneidervs.invoicecreator.service;

import com.github.schneidervs.invoicecreator.model.Department;
import com.github.schneidervs.invoicecreator.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }
}
