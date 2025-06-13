package com.github.schneidervs.invoicecreator.repository;

import com.github.schneidervs.invoicecreator.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {
    List<Invoice> findTop15ByOrderByIssueDateDesc();
}