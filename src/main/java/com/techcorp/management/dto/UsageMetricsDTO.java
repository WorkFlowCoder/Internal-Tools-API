package com.techcorp.management.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Indicateurs d'utilisation du tool")
public class UsageMetricsDto {

  @Schema(description = "Statistiquessur sur 30 derniers jours")
  @JsonProperty("last_30_days")
  private Last30DaysDto last30Days;
}
