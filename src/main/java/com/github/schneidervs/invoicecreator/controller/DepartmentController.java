package com.github.schneidervs.invoicecreator.controller;

import com.github.schneidervs.invoicecreator.model.Department;
import com.github.schneidervs.invoicecreator.service.DepartmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/departments")
@PreAuthorize("hasRole('ADMIN')")
public class DepartmentController {
    private static final String REDIRECT_DEPARTMENTS = "redirect:/departments";
    private static final String DEPARTMENT_ATTR = "department";

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public String manageDepartments(Model model) {
        model.addAttribute("departments", departmentService.findAll());
        return "departments/departments";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute(DEPARTMENT_ATTR, new Department());
        return "departments/new-department";
    }

    @PostMapping("/save")
    public String createDepartment(@ModelAttribute(DEPARTMENT_ATTR) Department department, Model model) {
        if (departmentService.departmentExists(department.getName())) {
            model.addAttribute("errorMessage", "Department with name '" + department.getName() + "' already exists.");
            model.addAttribute(DEPARTMENT_ATTR, department);
            return "departments/new-department";
        }
        departmentService.create(department);
        return REDIRECT_DEPARTMENTS;
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Department department = departmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid department Id:" + id));
        model.addAttribute(DEPARTMENT_ATTR, department);
        return "departments/edit-department";
    }

    @PostMapping("/update")
    public String updateDepartment(
            @RequestParam(value = "id", required = false) Long id,
            @ModelAttribute(DEPARTMENT_ATTR) Department updatedDepartment) {

        Department existingDepartment = departmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid department Id:" + id));

        existingDepartment.setName(updatedDepartment.getName());
        departmentService.update(existingDepartment);
        return REDIRECT_DEPARTMENTS;
    }

    @PostMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteById(id);
        return REDIRECT_DEPARTMENTS;
    }
}
