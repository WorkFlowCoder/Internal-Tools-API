package com.techcorp.management.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cost_tracking")
public class CostTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tool_id", nullable = false)
    @JsonIgnoreProperties("costTracking")
    private Tool tool;

    @Column(name="month_year")
    private LocalDateTime monthYear;

    @Column(name="total_monthly_cost")
    private Double totalMonthlyCost;

    @Column(name="active_users_count")
    private Integer activeUsersCcount;

    @Column(name="created_at")
    private LocalDateTime createdAt;
}

   