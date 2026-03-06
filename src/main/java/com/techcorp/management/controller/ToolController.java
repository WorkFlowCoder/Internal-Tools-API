package com.techcorp.management.controller;

import com.techcorp.management.entity.Tool;
import com.techcorp.management.entity.Category;
import com.techcorp.management.entity.ToolStatus;
import com.techcorp.management.service.ToolService;
import com.techcorp.management.entity.Department;
import com.techcorp.management.repository.ToolRepository;
import com.techcorp.management.dto.ToolDetailDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/tools")
@Tag(name = "Gestion des tools", description = "Endpoints pour l'inventaire et le filtrage des tools")
public class ToolController {

    private final ToolService toolService;
    private final ToolRepository toolRepository;

    public ToolController(ToolService toolService, ToolRepository toolRepository) {
        this.toolService = toolService;
        this.toolRepository = toolRepository;
    }

    @Operation(summary = "Récupérer un tool par ID", description = "Retourne les détails complets d'un tool spécifique.")
    @ApiResponse(responseCode = "200", description = "tool trouvé")
    @GetMapping("/{id}")
    public ResponseEntity<ToolDetailDTO> getToolById(@PathVariable Long id) {
        log.debug("Requête GET reçue pour obtenir le tool avec l'id : {}", id);
        return ResponseEntity.ok(toolService.getToolById(id));
    }

    @Operation(summary = "Créer un nouvel tool", description = "Ajoute un nouveau tool à l'inventaire.")
    @ApiResponse(responseCode = "201", description = "tool créé avec succès")
    @PostMapping
    public ResponseEntity<ToolDetailDTO> createTool(@Valid @RequestBody Tool tool){
        log.debug("Requête POST reçue pour créé le tool : {}", tool);
        ToolDetailDTO response = toolService.createTool(tool);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Modifier un tool existant", description = "Met à jour les informations d'un tool identifié par son ID.")
    @PutMapping("/{id}")
    public ResponseEntity<ToolDetailDTO> updateTool(@PathVariable Long id, @RequestBody Tool tool) {
        log.debug("Requête PUT reçue pour modifier le tool id : {}", id);
        return ResponseEntity.ok(toolService.updateTool(id, tool));
    }

    @Operation(summary = "Lister et filtrer les tools", description = "Récupère une liste des tools avec filtres optionnels et pagination.")
    @GetMapping
    public Map<String, Object> getTools(
        @Parameter(description = "Filtrer par département") @RequestParam(required = false) Department department,
        @Parameter(description = "Filtrer par statut") @RequestParam(required = false) ToolStatus status,
        @Parameter(description = "Coût minimum", example = "10.0") @RequestParam(required = false, name = "min_cost") Double minCost,
        @Parameter(description = "Coût maximum", example = "500.0") @RequestParam(required = false, name = "max_cost") Double maxCost,
        @Parameter(description = "Numéro de la page (0...N)") @RequestParam(required = false) Integer page,
        @Parameter(description = "Taille de la page") @RequestParam(required = false) Integer size,
        @Parameter(description = "Filtrer par catégorie") @RequestParam(required = false) Category categoryId
    ) {
        log.debug("Requête GET reçue pour obtenir les tools filtrés");
        
        List<ToolDetailDTO> content;
        long totalElements;
        Integer totalPages = null;
        Integer currentPage = null;

        content = toolService.getFilteredTools(department, status, minCost, maxCost, categoryId);
        totalElements = content.size();

        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<ToolDetailDTO> toolPage = toolService.getFilteredToolsPagination(department, status, minCost, maxCost, categoryId, pageable);
            content = toolPage.getContent();
            totalPages = toolPage.getTotalPages();
            currentPage = toolPage.getNumber();
        }

        Map<String, Object> filters = new HashMap<>();
        if (department != null) filters.put("department", department);
        if (status != null) filters.put("status", status);
        if (minCost != null) filters.put("min_cost", minCost);
        if (maxCost != null) filters.put("max_cost", maxCost);

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