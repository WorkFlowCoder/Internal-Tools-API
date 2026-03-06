package com.techcorp.management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

  @Column(name = "month_year")
  private LocalDateTime monthYear;

  @Column(name = "total_monthly_cost")
  private Double totalMonthlyCost;

  @Column(name = "active_users_count")
  private Integer activeUsersCcount;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
