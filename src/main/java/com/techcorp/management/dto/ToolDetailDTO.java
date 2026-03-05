package com.techcorp.management.dto;

import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ToolDetailDTO {
    private Long id;
    private String name;
    private String description;
    private String vendor;
    
    @JsonProperty("website_url")
    private String websiteUrl;
    
    private String category;
    
    @JsonProperty("monthly_cost")
    private Double monthlyCost;
    
    @JsonProperty("owner_department")
    private String ownerDepartment;
    
    private String status;
    
    @JsonProperty("active_users_count")
    private Long activeUsersCount;
    
    @JsonProperty("total_monthly_cost")
    private Double totalMonthlyCost;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("usage_metrics")
    private UsageMetricsDTO usageMetrics;
}