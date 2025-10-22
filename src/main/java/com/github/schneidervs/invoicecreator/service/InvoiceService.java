package com.github.schneidervs.invoicecreator.service;

import com.github.schneidervs.invoicecreator.model.Invoice;
import com.github.schneidervs.invoicecreator.model.MyCompany;
import com.github.schneidervs.invoicecreator.repository.InvoiceRepository;
import com.github.schneidervs.invoicecreator.InvoiceSpecifications;
import com.github.schneidervs.invoicecreator.repository.MyCompanyRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    private final InvoiceRepository repository;
    private final InvoiceSpecifications invoiceSpecifications;
    private final MyCompanyRepository myCompanyRepository;

    public InvoiceService(InvoiceRepository repository, InvoiceSpecifications invoiceSpecifications, MyCompanyRepository myCompanyRepository) {
        this.repository = repository;
        this.invoiceSpecifications = invoiceSpecifications;
        this.myCompanyRepository = myCompanyRepository;
    }

    public List<Invoice> getRecentInvoices() {
        return repository.findTop15ByOrderByIssueDateDesc();
    }

    public List<Invoice> searchInvoices(String query) {
        Specification<Invoice> spec = invoiceSpecifications.containsInAnyField(query);
        return repository.findAll(spec, Sort.by(Sort.Direction.DESC, "issueDate"));
    }

    public Invoice saveInvoice(Invoice invoice) {
        return repository.save(invoice);
    }

    public void saveTestInvoicesIfEmpty() {
        MyCompanyService myCompanyService = new MyCompanyService(myCompanyRepository);
        if (repository.count() == 0) {
            MyCompany defaultCompany = myCompanyService.findFirst();
            List<Invoice> testInvoices = List.of(
                    createTestInvoice("TEST-001", LocalDate.now(), "Client A", defaultCompany, "Service A",
                            new BigDecimal("100.00"), new BigDecimal("123.00"),
                            "One hundred twenty-three zloty", LocalDate.now().plusDays(14), "admin"),
                    createTestInvoice("TEST-002", LocalDate.now().minusDays(1), "Client B", defaultCompany, "Service B",
                            new BigDecimal("200.00"), new BigDecimal("246.00"),
                            "Two hundred forty-six zloty", LocalDate.now().plusDays(13), "admin"),
                    createTestInvoice("TEST-003", LocalDate.now().minusDays(2), "Client C", defaultCompany, "Service C",
                            new BigDecimal("300.00"), new BigDecimal("369.00"),
                            "Three hundred sixty-nine zloty", LocalDate.now().plusDays(12), "admin")
            );
            repository.saveAll(testInvoices);
        }
    }

    /** Вспомогательный метод для создания тестовой фактуры */
    private Invoice createTestInvoice(String number,
                                      LocalDate issueDate,
                                      String clientData,
                                      MyCompany myCompanyData,
                                      String serviceDescription,
                                      BigDecimal net,
                                      BigDecimal gross,
                                      String amountInWords,
                                      LocalDate dueDate,
                                      String createdBy) {
        Invoice inv = new Invoice();
        inv.setInvoiceNumber(number);
        inv.setIssueDate(issueDate);
        inv.setClientData(clientData);
        inv.setMyCompany(myCompanyData);
        inv.setServiceDescription(serviceDescription);
        inv.setNetAmount(net);
        inv.setGrossAmount(gross);
        inv.setAmountInWords(amountInWords);
        inv.setDueDate(dueDate);
        inv.setCreatedBy(createdBy);
        return inv;
    }

    public void deleteInvoice(Long id) {
        repository.deleteById(id);
    }

    public Invoice getInvoiceById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id " + id));
    }

    public Optional<Invoice> findInvoiceById(Long id) {
        return repository.findById(id);
    }
}
