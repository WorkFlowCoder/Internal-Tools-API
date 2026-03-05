package com.techcorp.management.dto;

import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class Last30DaysDTO {
    @JsonProperty("total_sessions")
    private Integer totalSessions;

    @JsonProperty("avg_session_minutes")
    private Integer avgSessionMinutes;
}