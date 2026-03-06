package com.techcorp.management.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Statistiques d'utilisation sur les 30 derniers jours")
public class Last30DaysDto {

  @Schema(description = "Nombre total de sessions ouvertes", example = "450")
  @JsonProperty("total_sessions")
  private Integer totalSessions;

  @Schema(description = "Durée moyenne d'une session", example = "25")
  @JsonProperty("avg_session_minutes")
  private Integer avgSessionMinutes;
}
