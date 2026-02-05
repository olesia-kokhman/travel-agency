package com.epam.finaltask.repository.specifications;

import com.epam.finaltask.model.entity.Order;
import com.epam.finaltask.repository.specifications.filters.OrderFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class OrderSpecification {

    private OrderSpecification() {}

    public static Specification<Order> build(OrderFilter filter) {
        return (root, query, cb) -> {
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return cb.and(predicates.toArray(new Predicate[0]));
            }

            if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
                predicates.add(root.get("status").in(filter.getStatuses()));
            }

            if (filter.getMinTotalAmount() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalAmount"), filter.getMinTotalAmount()));
            }

            if (filter.getMaxTotalAmount() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalAmount"), filter.getMaxTotalAmount()));
            }

            if (filter.getCreatedFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedFrom()));
            }

            if (filter.getCreatedTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getCreatedTo()));
            }

            if (filter.getHasPayment() != null) {
                if (filter.getHasPayment()) {
                    predicates.add(cb.isNotNull(root.get("payment")));
                } else {
                    predicates.add(cb.isNull(root.get("payment")));
                }
            }

            if (filter.getHasReview() != null) {
                if (filter.getHasReview()) {
                    predicates.add(cb.isNotNull(root.get("review")));
                } else {
                    predicates.add(cb.isNull(root.get("review")));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
