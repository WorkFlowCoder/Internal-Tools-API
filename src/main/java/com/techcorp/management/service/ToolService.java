package com.techcorp.management.service;

import com.techcorp.management.entity.Tool;
import com.techcorp.management.entity.Category;
import com.techcorp.management.entity.ToolStatus;
import com.techcorp.management.entity.Department;
import com.techcorp.management.repository.ToolRepository;
import com.techcorp.management.repository.ToolSpecifications;
import com.techcorp.management.exception.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;

import org.springframework.http.ResponseEntity;

import java.util.List;

@Service
public class ToolService {

    private final ToolRepository toolRepository;

    public ToolService(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    public List<Tool> getFilteredTools(Department dept, ToolStatus status, Double min, Double max, Category category) {
        Specification<Tool> spec = buildSpec(dept, status, min, max, category);
        return toolRepository.findAll(spec);
    }

    public Page<Tool> getFilteredToolsPagination(Department dept, ToolStatus status, Double min, Double max, Category category, Pageable pageable) {
        Specification<Tool> spec = buildSpec(dept, status, min, max, category);
        return toolRepository.findAll(spec, pageable);
    }

    public Tool getToolById(Long id) {
        return toolRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ID " + id + " not fund"));
    }

    private Specification<Tool> buildSpec(Department dept, ToolStatus status, Double min, Double max, Category category) {
        return Specification
            .where(ToolSpecifications.hasDepartment(dept))
            .and(ToolSpecifications.hasStatus(status))
            .and(ToolSpecifications.costBetween(min, max))
            .and(ToolSpecifications.hasCategory(category));
    }
}