package com.github.schneidervs.invoicecreator.service;

import com.github.schneidervs.invoicecreator.model.MyCompany;
import com.github.schneidervs.invoicecreator.repository.MyCompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyCompanyService {
    private final MyCompanyRepository myCompanyRepository;

    public MyCompanyService(MyCompanyRepository myCompanyRepository) {
        this.myCompanyRepository = myCompanyRepository;
    }

    public List<MyCompany> findAll() {
        return myCompanyRepository.findAll();
    }

    public Optional<MyCompany> findById(Long id) {
        return myCompanyRepository.findById(id);
    }

    public MyCompany findFirst() {
        List<MyCompany> companies = myCompanyRepository.findAll();
        return companies.isEmpty() ? null : companies.get(0);
    }

    public void create(MyCompany company) {
        myCompanyRepository.save(company);
    }

    public void update(MyCompany company) {
        myCompanyRepository.save(company);
    }

    public void deleteById(Long id) {
        myCompanyRepository.deleteById(id);
    }

    public boolean companyExists(String name) {
        return myCompanyRepository.findByName(name).isPresent();
    }

    public boolean nipExists(String nip) {
        return myCompanyRepository.existsByNip(nip);
    }

    public Optional<MyCompany> findByNip(String nip) {
        return myCompanyRepository.findByNip(nip);
    }
}
