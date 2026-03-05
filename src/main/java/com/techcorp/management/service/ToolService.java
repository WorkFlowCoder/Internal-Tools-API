package com.techcorp.management.service;

import com.techcorp.management.entity.Tool;
import com.techcorp.management.entity.Category;
import com.techcorp.management.entity.ToolStatus;
import com.techcorp.management.entity.Department;
import com.techcorp.management.entity.UsageLog;
import com.techcorp.management.repository.ToolRepository;
import com.techcorp.management.repository.ToolSpecifications;
import com.techcorp.management.exception.ResourceNotFoundException;

import com.techcorp.management.dto.ToolDetailDTO;
import com.techcorp.management.dto.UsageMetricsDTO;
import com.techcorp.management.dto.Last30DaysDTO;
import java.util.stream.Collectors;
import java.util.List;

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

    public List<ToolDetailDTO> getFilteredTools(Department dept, ToolStatus status, Double min, Double max, Category category) {
        Specification<Tool> spec = buildSpec(dept, status, min, max, category);
        List<Tool> tools = toolRepository.findAll(spec);
        return tools.stream()
                .map(this::mapToDetailDTO)
                .collect(Collectors.toList());
    }

    public Page<ToolDetailDTO> getFilteredToolsPagination(Department dept, ToolStatus status, Double min, Double max, Category category, Pageable pageable) {
        Specification<Tool> spec = buildSpec(dept, status, min, max, category);
        Page<Tool> toolPage = toolRepository.findAll(spec, pageable);
        return toolPage.map(this::mapToDetailDTO);
    }

    public ToolDetailDTO getToolById(Long id) {
        Tool tool = toolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tool with ID " + id + " not found"));
        return mapToDetailDTO(tool);
    }

    private Specification<Tool> buildSpec(Department dept, ToolStatus status, Double min, Double max, Category category) {
        return Specification
            .where(ToolSpecifications.hasDepartment(dept))
            .and(ToolSpecifications.hasStatus(status))
            .and(ToolSpecifications.costBetween(min, max))
            .and(ToolSpecifications.hasCategory(category));
    }

    public ToolDetailDTO mapToDetailDTO(Tool tool) {
        Last30DaysDTO last30Days = new Last30DaysDTO();
        List<UsageLog> logs = tool.getUsageLogs();
        
        int totalSessions = (logs != null) ? logs.size() : 0;
        double avgMinutes = (logs != null) ? logs.stream()
                .mapToInt(UsageLog::getMinutes)
                .average()
                .orElse(0.0) : 0.0;

        last30Days.setTotalSessions(totalSessions);
        last30Days.setAvgSessionMinutes((int) Math.round(avgMinutes));
        UsageMetricsDTO usageMetrics = new UsageMetricsDTO();
        usageMetrics.setLast30Days(last30Days);

        //Calcul unique users
        long activeUsersCount = (logs != null) ? logs.stream()
                .map(log -> log.getUser().getId())
                .distinct()
                .count() : 0;

        //Create DTO
        ToolDetailDTO dto = new ToolDetailDTO();
        dto.setId(tool.getId());
        dto.setName(tool.getName());
        dto.setDescription(tool.getDescription());
        dto.setVendor(tool.getVendor());
        dto.setWebsiteUrl(tool.getWebsiteUrl());
        
        //Verify disponibility of object
        dto.setCategory(tool.getCategory() != null ? tool.getCategory().getName() : null);
        dto.setOwnerDepartment(tool.getOwnerDepartment() != null ? tool.getOwnerDepartment().name() : null);
        dto.setStatus(tool.getStatus() != null ? tool.getStatus().name().toLowerCase() : null);
        
        //Calculate Cost
        dto.setMonthlyCost(tool.getMonthlyCost());
        dto.setActiveUsersCount(activeUsersCount);
        dto.setTotalMonthlyCost(activeUsersCount * tool.getMonthlyCost()); // Le calcul demandé !
        
        //Add date
        dto.setCreatedAt(tool.getCreatedAt());
        dto.setUpdatedAt(tool.getUpdatedAt());

        dto.setUsageMetrics(usageMetrics);
        return dto;
    }
}