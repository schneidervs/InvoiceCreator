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
        return "invoices/create-invoice";
    }

    @PostMapping("/save")
    public String saveInvoice(@ModelAttribute("invoice") Invoice invoice) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        invoice.setCreatedBy(auth.getName());

        invoiceService.saveInvoice(invoice);

        return "redirect:/invoices";
    }

    @PostMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return "redirect:/invoices";
    }
}
