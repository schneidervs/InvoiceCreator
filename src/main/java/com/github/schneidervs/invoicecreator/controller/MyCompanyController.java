package com.github.schneidervs.invoicecreator.controller;

import com.github.schneidervs.invoicecreator.model.MyCompany;
import com.github.schneidervs.invoicecreator.service.MyCompanyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/my-company")
@PreAuthorize("hasRole('ADMIN')")
public class MyCompanyController {
    private static final String REDIRECT_MY_COMPANY = "redirect:/my-company";
    private static final String COMPANY_ATTR = "company";
    private static final String NEW_COMPANY = "my-company/new-company";

    private final MyCompanyService myCompanyService;

    public MyCompanyController(MyCompanyService myCompanyService) {
        this.myCompanyService = myCompanyService;
    }

    @GetMapping
    public String manageCompany(Model model) {
        model.addAttribute("companies", myCompanyService.findAll());
        return "my-company/companies";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute(COMPANY_ATTR, new MyCompany());
        return NEW_COMPANY;
    }

    @PostMapping("/save")
    public String createCompany(@ModelAttribute(COMPANY_ATTR) MyCompany company, Model model) {
        if (myCompanyService.companyExists(company.getName())) {
            model.addAttribute("errorMessage", "Company with name '" + company.getName() + "' already exists.");
            model.addAttribute(COMPANY_ATTR, company);
            return NEW_COMPANY;
        }
        if (company.getNip() != null && !company.getNip().isEmpty() && myCompanyService.nipExists(company.getNip())) {
            model.addAttribute("errorMessage", "Company with NIP '" + company.getNip() + "' already exists.");
            model.addAttribute(COMPANY_ATTR, company);
            return NEW_COMPANY;
        }
        myCompanyService.create(company);
        return REDIRECT_MY_COMPANY;
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        MyCompany company = myCompanyService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid company Id:" + id));
        model.addAttribute(COMPANY_ATTR, company);
        return "my-company/edit-company";
    }

    @PostMapping("/update")
    public String updateCompany(
            @RequestParam(value = "id", required = false) Long id,
            @ModelAttribute(COMPANY_ATTR) MyCompany updatedCompany) {

        MyCompany existingCompany = myCompanyService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid company Id:" + id));

        // Проверка на дублирование имени (если имя изменилось)
        if (!existingCompany.getName().equals(updatedCompany.getName()) &&
            myCompanyService.companyExists(updatedCompany.getName())) {
            return "redirect:/my-company/edit/" + id + "?error=name_exists";
        }

        // Проверка на дублирование NIP (если NIP изменился)
        if (updatedCompany.getNip() != null && !updatedCompany.getNip().isEmpty() &&
            !updatedCompany.getNip().equals(existingCompany.getNip()) &&
            myCompanyService.nipExists(updatedCompany.getNip())) {
            return "redirect:/my-company/edit/" + id + "?error=nip_exists";
        }

        existingCompany.setName(updatedCompany.getName());
        existingCompany.setStreet(updatedCompany.getStreet());
        existingCompany.setHouseNumber(updatedCompany.getHouseNumber());
        existingCompany.setPostalCode(updatedCompany.getPostalCode());
        existingCompany.setCity(updatedCompany.getCity());
        existingCompany.setPhoneFax(updatedCompany.getPhoneFax());
        existingCompany.setRegion(updatedCompany.getRegion());
        existingCompany.setNip(updatedCompany.getNip());
        existingCompany.setBdoRegistrationNumber(updatedCompany.getBdoRegistrationNumber());
        existingCompany.setBankName(updatedCompany.getBankName());
        existingCompany.setBankAccountNumber(updatedCompany.getBankAccountNumber());

        myCompanyService.update(existingCompany);
        return REDIRECT_MY_COMPANY;
    }

    @PostMapping("/delete/{id}")
    public String deleteCompany(@PathVariable Long id) {
        myCompanyService.deleteById(id);
        return REDIRECT_MY_COMPANY;
    }
}
