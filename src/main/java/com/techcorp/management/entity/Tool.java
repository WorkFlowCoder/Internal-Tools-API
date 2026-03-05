package com.techcorp.management.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tools")
public class Tool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String vendor;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "monthly_cost")
    private Double monthlyCost;

    @Column(name = "active_users_count")
    private Integer activeUsersCount;

    @Column(name = "owner_department")
    private String ownerDepartement;

    private String status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}