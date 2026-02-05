package com.epam.finaltask.repository.specifications;

import com.epam.finaltask.model.entity.Payment;
import com.epam.finaltask.repository.specifications.filters.PaymentFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class PaymentSpecification {

    private PaymentSpecification() {}

    public static Specification<Payment> build(PaymentFilter filter) {
        return (root, query, cb) -> {
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return cb.and(predicates.toArray(new Predicate[0]));
            }

            if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
                predicates.add(root.get("status").in(filter.getStatuses()));
            }

            if (filter.getMethods() != null && !filter.getMethods().isEmpty()) {
                predicates.add(root.get("paymentMethod").in(filter.getMethods()));
            }

            if (filter.getMinAmount() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), filter.getMinAmount()));
            }

            if (filter.getMaxAmount() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), filter.getMaxAmount()));
            }

            if (filter.getPaidFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("paidAt"), filter.getPaidFrom()));
            }

            if (filter.getPaidTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("paidAt"), filter.getPaidTo()));
            }

            if (filter.getHasFailureReason() != null) {
                if (filter.getHasFailureReason()) {
                    predicates.add(cb.isNotNull(root.get("failureReason")));
                    predicates.add(cb.notEqual(cb.trim(root.get("failureReason")), ""));
                } else {
                    predicates.add(cb.or(
                            cb.isNull(root.get("failureReason")),
                            cb.equal(cb.trim(root.get("failureReason")), "")
                    ));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
