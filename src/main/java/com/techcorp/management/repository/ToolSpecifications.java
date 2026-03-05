package com.techcorp.management.repository;

import com.techcorp.management.entity.Tool;
import com.techcorp.management.entity.Category;
import com.techcorp.management.entity.ToolStatus;
import com.techcorp.management.entity.Department;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;

public class ToolSpecifications {

    public static Specification<Tool> hasDepartment(Department dept) {
        return (root, query, cb) -> dept == null ? null : cb.equal(root.get("ownerDepartment"), dept);
    }

    public static Specification<Tool> hasStatus(ToolStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Tool> costBetween(Double min, Double max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            if (min != null && max != null) return cb.between(root.get("monthlyCost"), min, max);
            if (min != null) return cb.greaterThanOrEqualTo(root.get("monthlyCost"), min);
            return cb.lessThanOrEqualTo(root.get("monthlyCost"), max);
        };
    }

    public static Specification<Tool> hasCategory(Category category) {
        return (root, query, cb) -> category == null ? null : cb.equal(root.get("category").get("id"), category);
    }
}