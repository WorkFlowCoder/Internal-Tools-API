package com.techcorp.management.controller;

import com.techcorp.management.entity.Tool;
import com.techcorp.management.service.ToolService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tools")
public class ToolController {

    private final ToolService toolService;

    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }

    @GetMapping
    public List<Tool> getTools(
        @RequestParam(required = false) String department,
        @RequestParam(required = false) String status,
        @RequestParam(required = false, name = "min_cost") Double minCost,
        @RequestParam(required = false, name = "max_cost") Double maxCost,
        @RequestParam(required = false) Integer categoryId
    ) {
        return toolService.getFilteredTools(department, status, minCost, maxCost, categoryId);
    }
}