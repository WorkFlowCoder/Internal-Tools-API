package com.techcorp.management.service;

import com.techcorp.management.entity.Tool;
import com.techcorp.management.repository.ToolRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ToolService {

    private final ToolRepository toolRepository;

    public ToolService(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    public List<Tool> getFilteredTools(String dept, String status, Double min, Double max, Integer categoryId) {
        if (dept != null && status != null) {
            return toolRepository.findByOwnerDepartementAndStatus(dept, status);
        }
        
        if (min != null && max != null && categoryId != null) {
            return toolRepository.findByMonthlyCostBetweenAndCategoryId(min, max, categoryId);
        }
        
        return toolRepository.findAll();
    }
}