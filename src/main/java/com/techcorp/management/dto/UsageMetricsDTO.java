package com.techcorp.management.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Indicateurs d'utilisation du tool")
public class UsageMetricsDTO {

    @Schema(description = "Statistiquessur sur 30 derniers jours")
    @JsonProperty("last_30_days")
    private Last30DaysDTO last30Days;
}