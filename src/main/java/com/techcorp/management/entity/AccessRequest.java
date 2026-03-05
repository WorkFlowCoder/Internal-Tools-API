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

    @Column(name="business_justification")
    private String businessJustification;

    @Enumerated(EnumType.STRING)
    private AccessStatus status;

    @Column(name="requested_at")
    private LocalDateTime requestedAt;

    @Column(name="processed_at")
    private LocalDateTime processedAt;

    @Column(name="processed_by")
    private Integer processedBy;

    @Column(name="processing_notes")
    private String notes;
}

   