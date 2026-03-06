package com.techcorp.management.dto;

import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Détails complets d'un tool")
public class ToolDetailDTO {

    @Schema(description = "Identifiant unique du tool", example = "101")
    private Long id;

    @Schema(description = "Nom du tool", example = "Slack")
    private String name;

    @Schema(description = "Description des fonctionnalités", example = "Plateforme de communication collaborative")
    private String description;

    @Schema(description = "Éditeur du logiciel", example = "Salesforce")
    private String vendor;
    
    @Schema(description = "URL officielle du tool", example = "https://slack.com")
    @JsonProperty("website_url")
    private String websiteUrl;
    
    @Schema(description = "Catégorie du tool", example = "Communication")
    private String category;
    
    @Schema(description = "Coût unitaire par mois", example = "12.50")
    @JsonProperty("monthly_cost")
    private Double monthlyCost;
    
    @Schema(description = "Département responsable du tool", example = "IT / Engineering")
    @JsonProperty("owner_department")
    private String ownerDepartment;
    
    @Schema(description = "Statut actuel du tool", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "PENDING_REVIEW"})
    private String status;
    
    @Schema(description = "Nombre d'utilisateurs actifs", example = "150")
    @JsonProperty("active_users_count")
    private Long activeUsersCount;
    
    @Schema(description = "Coût total calculé (Coût x Utilisateurs)", example = "1875.00")
    @JsonProperty("total_monthly_cost")
    private Double totalMonthlyCost;
    
    @Schema(description = "Date d'enregistrement dans le système")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    @Schema(description = "Date de la dernière mise à jour")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @Schema(description = "Statistiques d'utilisation détaillées")
    @JsonProperty("usage_metrics")
    private UsageMetricsDTO usageMetrics;
}