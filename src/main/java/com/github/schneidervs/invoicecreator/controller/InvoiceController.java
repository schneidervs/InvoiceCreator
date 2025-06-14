package com.github.schneidervs.invoicecreator.controller;

import com.github.schneidervs.invoicecreator.model.Invoice;
import com.github.schneidervs.invoicecreator.service.InvoiceService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {
    private static final String REDIRECT_INVOICES = "redirect:/invoices";

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("")
    public String viewInvoices(
            @RequestParam(required = false)
            String query, Model model) {
        invoiceService.saveTestInvoicesIfEmpty();

        List<Invoice> invoices = (query!=null && !query.isBlank()) ?
                invoiceService.searchInvoices(query) :
                invoiceService.getRecentInvoices();

        model.addAttribute("invoices", invoices);
        model.addAttribute("query", query);
        return "invoices/invoices";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("dueDate", LocalDate.now().plusDays(14));
        return "invoices/new-invoice";
    }

    @PostMapping("/save")
    public String saveInvoice(@ModelAttribute("invoice") Invoice invoice) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        invoice.setCreatedBy(auth.getName());
        invoiceService.saveInvoice(invoice);
        return REDIRECT_INVOICES;
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Invoice invoice = invoiceService.getInvoiceById(id); // метод нужно создать
        model.addAttribute("invoice", invoice);
        return "invoices/edit-invoice";
    }

    @PostMapping("/update")
    public String updateInvoice(@RequestParam(value = "id", required = false) Long id, //kostyl
                                @ModelAttribute("invoice") Invoice updatedInvoice) {

        Invoice existingInvoice = invoiceService.findInvoiceById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + updatedInvoice.getId()));
        existingInvoice.setInvoiceNumber(updatedInvoice.getInvoiceNumber());
        existingInvoice.setIssueDate(updatedInvoice.getIssueDate());
        existingInvoice.setClientData(updatedInvoice.getClientData());
        existingInvoice.setCompanyData(updatedInvoice.getCompanyData());
        existingInvoice.setServiceDescription(updatedInvoice.getServiceDescription());
        existingInvoice.setNetAmount(updatedInvoice.getNetAmount());
        existingInvoice.setGrossAmount(updatedInvoice.getGrossAmount());
        existingInvoice.setAmountInWords(updatedInvoice.getAmountInWords());
        existingInvoice.setDueDate(updatedInvoice.getDueDate());
        existingInvoice.setCreatedBy(updatedInvoice.getCreatedBy());

        invoiceService.saveInvoice(existingInvoice);
        return REDIRECT_INVOICES;
    }

    @PostMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return REDIRECT_INVOICES;
    }
}
