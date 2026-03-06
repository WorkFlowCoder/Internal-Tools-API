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
@Table(name = "user_tool_access")
public class UserToolAccess {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnoreProperties("userToolAccess")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tool_id", nullable = false)
  @JsonIgnoreProperties("userToolAccess")
  private Tool tool;

  @Column(name = "granted_at")
  private LocalDateTime grantedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "granted_by", nullable = false)
  @JsonIgnoreProperties("userToolAccess")
  private User grantedBy;

  @Column(name = "revoked_at")
  private LocalDateTime revokedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "revoked_by")
  @JsonIgnoreProperties("userToolAccess")
  private User revokedBy;

  @Enumerated(EnumType.STRING)
  private UserToolStatus status;
}
