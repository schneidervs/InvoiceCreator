package com.github.schneidervs.invoicecreator;

import com.github.schneidervs.invoicecreator.model.Invoice;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InvoiceSpecifications {
    private static final String CREATOR_BY_USER = "createdByUser";

    public Specification<Invoice> containsInAnyField(String text) {
        return (root, query, cb) -> {
            if (text == null || text.isBlank()) {
                return cb.conjunction();
            }
            String pattern = "%" + text.toLowerCase() + "%";
            List<Predicate> preds = new ArrayList<>();
            preds.add(cb.like(cb.lower(root.get("invoiceNumber")),    pattern));
            preds.add(cb.like(cb.lower(root.get("clientData")),       pattern));
            preds.add(cb.like(cb.lower(root.get("myCompany").get("name")),      pattern));
            preds.add(cb.like(cb.lower(root.get("serviceDescription")), pattern));
            preds.add(cb.like(cb.lower(root.get("amountInWords")),    pattern));
            preds.add(cb.like(cb.lower(root.get(CREATOR_BY_USER).get("fullName")), pattern));
            preds.add(cb.like(cb.lower(root.get(CREATOR_BY_USER).get("userData").get("firstName")), pattern));
            preds.add(cb.like(cb.lower(root.get(CREATOR_BY_USER).get("userData").get("lastName")), pattern));
            return cb.or(preds.toArray(new Predicate[0]));
        };
    }
}
