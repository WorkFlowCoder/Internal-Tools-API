package com.techcorp.management.dto;

import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class UsageMetricsDTO {
    @JsonProperty("last_30_days")
    private Last30DaysDTO last30Days;
}