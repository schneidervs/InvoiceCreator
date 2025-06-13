package com.github.schneidervs.invoicecreator.controller;

import com.github.schneidervs.invoicecreator.model.Invoice;
import com.github.schneidervs.invoicecreator.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            @RequestParam(required = false) String query,
            Model model) {

        invoiceService.saveTestInvoicesIfEmpty();

        List<Invoice> invoices = (query!=null && !query.isBlank()) ? invoiceService.searchInvoices(query) : invoiceService.getRecentInvoices();

        model.addAttribute("invoices", invoices);
        model.addAttribute("query", query);
        return "invoices";
    }
}
