package com.techcorp.management.controller;

import com.techcorp.management.entity.Tool;
import com.techcorp.management.entity.Category;
import com.techcorp.management.entity.ToolStatus;
import com.techcorp.management.service.ToolService;
import com.techcorp.management.entity.Department;
import com.techcorp.management.repository.ToolRepository;
import com.techcorp.management.dto.ToolDetailDTO;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/tools")
public class ToolController {

    private final ToolService toolService;
    private final ToolRepository toolRepository;

    public ToolController(ToolService toolService,ToolRepository toolRepository) {
        this.toolService = toolService;
        this.toolRepository = toolRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToolDetailDTO> getToolById(@PathVariable Long id) {
        ToolDetailDTO tool = toolService.getToolById(id);
        return ResponseEntity.ok(tool);
    }

    @PostMapping
    public ResponseEntity<ToolDetailDTO> createTool(@Valid @RequestBody Tool tool){
        ToolDetailDTO response = toolService.createTool(tool);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ToolDetailDTO> updateTool(@PathVariable Long id, @RequestBody Tool tool) {
        ToolDetailDTO updated = toolService.updateTool(id, tool);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public Map<String, Object> getTools(
        @RequestParam(required = false) Department department,
        @RequestParam(required = false) ToolStatus status,
        @RequestParam(required = false, name = "min_cost") Double minCost,
        @RequestParam(required = false, name = "max_cost") Double maxCost,
        @RequestParam(required = false) Integer page, // Pagination
        @RequestParam(required = false) Integer size, // Pagination
        @RequestParam(required = false) Category categoryId
    ) {
        List<ToolDetailDTO> content;
        long totalElements;
        Integer totalPages = null;
        Integer currentPage = null;

        // Récupère le résultat du filtrage
        content = toolService.getFilteredTools(department, status, minCost, maxCost, categoryId);
        totalElements = content.size();
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<ToolDetailDTO> toolPage = toolService.getFilteredToolsPagination(department, status, minCost, maxCost, categoryId, pageable);
            content = toolPage.getContent();
            totalPages = toolPage.getTotalPages();
            currentPage = toolPage.getNumber();
        }

        // Les filtres utilisés
        Map<String, Object> filters = new HashMap<>();
        if (department != null) filters.put("department", department);
        if (status != null) filters.put("status", status);
        if (minCost != null) filters.put("min_cost", minCost);
        if (maxCost != null) filters.put("max_cost", maxCost);

        // Création du message
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", content);
        response.put("total", toolRepository.count());
        response.put("filtered", totalElements);
        response.put("filters_applied", filters);

        if (currentPage != null) {
            response.put("current_page", currentPage);
            response.put("total_pages", totalPages);
        }

        return response;
    }
}