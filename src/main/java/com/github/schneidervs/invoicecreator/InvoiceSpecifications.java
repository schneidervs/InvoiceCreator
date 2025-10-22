package com.github.schneidervs.invoicecreator;

import com.github.schneidervs.invoicecreator.model.Invoice;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InvoiceSpecifications {

    public Specification<Invoice> containsInAnyField(String text) {
        return (root, query, cb) -> {
            if (text == null || text.isBlank()) {
                return cb.conjunction();
            }
            String pattern = "%" + text.toLowerCase() + "%";
            List<Predicate> preds = new ArrayList<>();
            preds.add(cb.like(cb.lower(root.get("invoiceNumber")),    pattern));
            preds.add(cb.like(cb.lower(root.get("clientData")),       pattern));
            preds.add(cb.like(cb.lower(root.get("myCompanyData").get("name")),      pattern));
            preds.add(cb.like(cb.lower(root.get("serviceDescription")), pattern));
            preds.add(cb.like(cb.lower(root.get("amountInWords")),    pattern));
            return cb.or(preds.toArray(new Predicate[0]));
        };
    }
}
