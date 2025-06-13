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

    @GetMapping
    public String viewInvoices(
            @RequestParam(required = false)
            String query, Model model) {
        invoiceService.saveTestInvoicesIfEmpty();

        List<Invoice> invoices = (query!=null && !query.isBlank()) ?
                invoiceService.searchInvoices(query) :
                invoiceService.getRecentInvoices();

        model.addAttribute("invoices", invoices);
        model.addAttribute("query", query);
        return "invoices";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("dueDate", LocalDate.now().plusDays(14));
        return "create-invoice";
    }

    @PostMapping("/save")
    public String saveInvoice(@ModelAttribute("invoice") Invoice invoice) {
        // Устанавливаем имя текущего пользователя
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        invoice.setCreatedBy(auth.getName());

        // Сохраняем фактуру
        invoiceService.saveInvoice(invoice);

        // Перенаправляем на страницу со списком фактур
        return "redirect:/invoices";
    }
}
