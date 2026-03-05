package com.techcorp.management.service;

import com.techcorp.management.entity.Tool;
import com.techcorp.management.entity.Category;
import com.techcorp.management.entity.ToolStatus;
import com.techcorp.management.entity.ToolDepartment;
import com.techcorp.management.repository.ToolRepository;
import com.techcorp.management.repository.ToolSpecifications;
import org.springframework.stereotype.Service;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

@Service
public class ToolService {

    private final ToolRepository toolRepository;

    public ToolService(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    public List<Tool> getFilteredTools(ToolDepartment dept, ToolStatus status, Double min, Double max, Category categoryId) {

        Specification<Tool> spec = Specification
            .where(ToolSpecifications.hasDepartment(dept))
            .and(ToolSpecifications.hasStatus(status))
            .and(ToolSpecifications.costBetween(min, max))
            .and(ToolSpecifications.hasCategory(categoryId));

        return toolRepository.findAll(spec);
    }
}