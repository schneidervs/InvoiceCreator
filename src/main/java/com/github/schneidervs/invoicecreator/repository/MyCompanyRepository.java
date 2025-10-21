package com.github.schneidervs.invoicecreator.repository;

import com.github.schneidervs.invoicecreator.model.MyCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyCompanyRepository extends JpaRepository<MyCompany, Long> {
    Optional<MyCompany> findByName(String name);
    Optional<MyCompany> findByNip(String nip);
    boolean existsByNip(String nip);
}
