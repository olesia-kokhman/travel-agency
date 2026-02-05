package com.epam.finaltask.repository.specifications;

import com.epam.finaltask.model.entity.User;
import com.epam.finaltask.repository.specifications.filters.UserFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class UserSpecification {

    private UserSpecification() {}

    public static Specification<User> build(UserFilter filter) {
        return (root, query, cb) -> {
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return cb.and(predicates.toArray(new Predicate[0]));
            }

            if (filter.getQ() != null && !filter.getQ().isBlank()) {
                String like = "%" + filter.getQ().trim().toLowerCase() + "%";

                Predicate nameLike = cb.like(cb.lower(root.get("name")), like);
                Predicate surnameLike = cb.like(cb.lower(root.get("surname")), like);
                Predicate emailLike = cb.like(cb.lower(root.get("email")), like);
                Predicate phoneLike = cb.like(cb.lower(root.get("phoneNumber")), like);

                predicates.add(cb.or(nameLike, surnameLike, emailLike, phoneLike));
            }

            if (filter.getRoles() != null && !filter.getRoles().isEmpty()) {
                predicates.add(root.get("role").in(filter.getRoles()));
            }

            if (filter.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), filter.getActive()));
            }

            if (filter.getMinBalance() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("balance"), filter.getMinBalance()));
            }

            if (filter.getMaxBalance() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("balance"), filter.getMaxBalance()));
            }

            if (filter.getCreatedFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedFrom()));
            }

            if (filter.getCreatedTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getCreatedTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
