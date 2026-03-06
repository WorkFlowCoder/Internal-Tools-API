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
@Table(name = "usage_logs")
public class UsageLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnoreProperties("usageLogs")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tool_id", nullable = false)
  @JsonIgnoreProperties("usageLogs")
  private Tool tool;

  @Column(name = "session_date")
  private LocalDateTime sessionDate;

  @Column(name = "usage_minutes")
  private Integer minutes;

  @Column(name = "actions_count")
  private Integer nbAction;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
