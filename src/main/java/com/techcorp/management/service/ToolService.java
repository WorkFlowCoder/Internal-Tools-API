package com.techcorp.management.service;

import com.techcorp.management.dto.Last30DaysDto;
import com.techcorp.management.dto.ToolDetailDto;
import com.techcorp.management.dto.UsageMetricsDto;
import com.techcorp.management.entity.Category;
import com.techcorp.management.entity.Department;
import com.techcorp.management.entity.Tool;
import com.techcorp.management.entity.ToolStatus;
import com.techcorp.management.entity.UsageLog;
import com.techcorp.management.exception.BadRequestException;
import com.techcorp.management.exception.ResourceNotFoundException;
import com.techcorp.management.repository.CategoryRepository;
import com.techcorp.management.repository.ToolRepository;
import com.techcorp.management.repository.ToolSpecifications;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ToolService {

  private final ToolRepository toolRepository;
  private final CategoryRepository categoryRepository;

  public ToolService(ToolRepository toolRepository, CategoryRepository categoryRepository) {
    this.toolRepository = toolRepository;
    this.categoryRepository = categoryRepository;
  }

  public List<ToolDetailDto> getFilteredTools(
      Department dept, ToolStatus status, Double min, Double max, Category category) {
    log.info("Tentative de récupération des tools");
    Specification<Tool> spec = buildSpec(dept, status, min, max, category);
    List<Tool> tools = toolRepository.findAll(spec);
    return tools.stream().map(this::mapToDetailDto).collect(Collectors.toList());
  }

  public Page<ToolDetailDto> getFilteredToolsPagination(
      Department dept,
      ToolStatus status,
      Double min,
      Double max,
      Category category,
      Pageable pageable) {
    log.info("Tentative de récupération des tools (avec pagination)");
    Specification<Tool> spec = buildSpec(dept, status, min, max, category);
    Page<Tool> toolPage = toolRepository.findAll(spec, pageable);
    return toolPage.map(this::mapToDetailDto);
  }

  public ToolDetailDto getToolById(Long id) {
    log.info("Tentative de récupération du tool avec l'id : {}", id);
    Tool tool =
        toolRepository
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException("Le tool avec l'id " + id + " n'existe pas!"));
    return mapToDetailDto(tool);
  }

  private Specification<Tool> buildSpec(
      Department dept, ToolStatus status, Double min, Double max, Category category) {
    return Specification.where(ToolSpecifications.hasDepartment(dept))
        .and(ToolSpecifications.hasStatus(status))
        .and(ToolSpecifications.costBetween(min, max))
        .and(ToolSpecifications.hasCategory(category));
  }

  public ToolDetailDto mapToDetailDto(Tool tool) {
    Last30DaysDto last30Days = new Last30DaysDto();
    List<UsageLog> logs = tool.getUsageLogs();

    int totalSessions = (logs != null) ? logs.size() : 0;
    double avgMinutes =
        (logs != null) ? logs.stream().mapToInt(UsageLog::getMinutes).average().orElse(0.0) : 0.0;

    last30Days.setTotalSessions(totalSessions);
    last30Days.setAvgSessionMinutes((int) Math.round(avgMinutes));
    UsageMetricsDto usageMetrics = new UsageMetricsDto();
    usageMetrics.setLast30Days(last30Days);

    // Calcul unique users
    long activeUsersCount =
        (logs != null) ? logs.stream().map(log -> log.getUser().getId()).distinct().count() : 0;

    // Create Dto
    ToolDetailDto dto = new ToolDetailDto();
    dto.setId(tool.getId());
    dto.setName(tool.getName());
    dto.setDescription(tool.getDescription());
    dto.setVendor(tool.getVendor());
    dto.setWebsiteUrl(tool.getWebsiteUrl());

    // Verify disponibility of object
    dto.setCategory(tool.getCategory() != null ? tool.getCategory().getName() : null);
    dto.setOwnerDepartment(
        tool.getOwnerDepartment() != null ? tool.getOwnerDepartment().name() : null);
    dto.setStatus(tool.getStatus() != null ? tool.getStatus().name().toLowerCase() : null);

    // Calculate Cost
    dto.setMonthlyCost(tool.getMonthlyCost());
    dto.setActiveUsersCount(activeUsersCount);
    dto.setTotalMonthlyCost(activeUsersCount * tool.getMonthlyCost());

    // Add date
    dto.setCreatedAt(tool.getCreatedAt());
    dto.setUpdatedAt(tool.getUpdatedAt());

    dto.setUsageMetrics(usageMetrics);
    return dto;
  }

  public ToolDetailDto createTool(Tool tool) {
    log.info("Tentative d'ajout d'un tool");
    if (toolRepository.existsByName(tool.getName())) {
      throw new BadRequestException("Un outil avec le nom '" + tool.getName() + "' existe déjà.");
    }
    if (tool.getMonthlyCost() != null && tool.getMonthlyCost() < 0) {
      throw new BadRequestException("Cout doit être >=0 et 2 digit maximum !");
    }
    this.validateCost(tool.getMonthlyCost());
    if (tool.getCategory() != null && tool.getCategory().getId() != null) {
      Category categorie =
          this.categoryRepository
              .findById(tool.getCategory().getId())
              .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée"));
      tool.setCategory(categorie);
    }

    tool.setId(null);
    if (tool.getStatus() == null) {
      tool.setStatus(ToolStatus.active);
    }
    Tool savedTool = toolRepository.save(tool);
    log.info("Tool créé avec succès avec l'id : {}", savedTool.getId());
    return mapToDetailDto(savedTool);
  }

  private void validateCost(Double cost) {
    String text = Double.toString(Math.abs(cost));
    int integerPlaces = text.indexOf('.');
    int decimalPlaces = text.length() - integerPlaces - 1;

    if (decimalPlaces > 2) {
      throw new BadRequestException("Cout doit être >=0 et 2 digit maximum !");
    }
  }

  public ToolDetailDto updateTool(Long id, Tool toolDetails) {
    log.info("Tentative de modification d'un tool");
    Tool existingTool =
        toolRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Aucun tool trouvé avec l'id " + id));
    // Changement description
    if (toolDetails.getDescription() != null) {
      existingTool.setDescription(toolDetails.getDescription());
    }
    // Changement cout
    if (toolDetails.getMonthlyCost() != null) {
      if (toolDetails.getMonthlyCost() < 0) {
        throw new BadRequestException("Cout ne peut être négatif !");
      }
      existingTool.setMonthlyCost(toolDetails.getMonthlyCost());
    }
    // Le status
    if (toolDetails.getStatus() != null) {
      existingTool.setStatus(toolDetails.getStatus());
    }
    Tool updatedTool = toolRepository.save(existingTool);
    log.info("Tool modifié avec succès avec l'id : {}", updatedTool.getId());
    return mapToDetailDto(updatedTool);
  }
}
