package com.techcorp.management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "access_requests")
public class AccessRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnoreProperties("accessRequest")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tool_id", nullable = false)
  @JsonIgnoreProperties("accessRequest")
  private Tool tool;

  @Column(name = "business_justification")
  private String businessJustification;

  @Enumerated(EnumType.STRING)
  private AccessStatus status;

  @Column(name = "requested_at")
  private LocalDateTime requestedAt;

  @Column(name = "processed_at")
  private LocalDateTime processedAt;

  @Column(name = "processed_by")
  private Integer processedBy;

  @Column(name = "processing_notes")
  private String notes;
}
