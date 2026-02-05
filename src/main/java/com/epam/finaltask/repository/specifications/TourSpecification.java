package com.epam.finaltask.repository.specifications;

import com.epam.finaltask.model.entity.Tour;
import com.epam.finaltask.repository.specifications.filters.TourFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class TourSpecification {

    private TourSpecification() {}

    public static Specification<Tour> build(TourFilter filter) {
        return (root, query, cb) -> {
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return cb.and(predicates.toArray(new Predicate[0]));
            }

            if (filter.getQ() != null && !filter.getQ().isBlank()) {
                String like = "%" + filter.getQ().trim().toLowerCase() + "%";

                Predicate titleLike = cb.like(cb.lower(root.get("title")), like);
                Predicate shortDescLike = cb.like(cb.lower(root.get("shortDescription")), like);
                Predicate longDescLike = cb.like(cb.lower(root.get("longDescription")), like);

                predicates.add(cb.or(titleLike, shortDescLike, longDescLike));
            }

            if (filter.getTypes() != null && !filter.getTypes().isEmpty()) {
                predicates.add(root.get("tourType").in(filter.getTypes()));
            }

            if (filter.getTransferTypes() != null && !filter.getTransferTypes().isEmpty()) {
                predicates.add(root.get("transferType").in(filter.getTransferTypes()));
            }

            if (filter.getHotelTypes() != null && !filter.getHotelTypes().isEmpty()) {
                predicates.add(root.get("hotelType").in(filter.getHotelTypes()));
            }

            if (filter.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
            }
            if (filter.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
            }

            if (filter.getCountry() != null && !filter.getCountry().isBlank()) {
                String like = "%" + filter.getCountry().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("country")), like));
            }

            if (filter.getCity() != null && !filter.getCity().isBlank()) {
                String like = "%" + filter.getCity().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("city")), like));
            }

            if (filter.getHot() != null) {
                predicates.add(cb.equal(root.get("hot"), filter.getHot()));
            }

            if (filter.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), filter.getActive()));
            }

            if (filter.getMinCapacity() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("capacity"), filter.getMinCapacity()));
            }

            if (filter.getMaxCapacity() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("capacity"), filter.getMaxCapacity()));
            }

            if (filter.getCheckInFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("checkIn"), filter.getCheckInFrom()));
            }

            if (filter.getCheckInTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("checkIn"), filter.getCheckInTo()));
            }

            if (filter.getCheckOutFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("checkOut"), filter.getCheckOutFrom()));
            }

            if (filter.getCheckOutTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("checkOut"), filter.getCheckOutTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
