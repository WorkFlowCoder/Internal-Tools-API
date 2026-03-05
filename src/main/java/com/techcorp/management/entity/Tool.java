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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category categoryId;

    @Column(name = "monthly_cost")
    private Double monthlyCost;

    @Column(name = "active_users_count")
    private Integer activeUsersCount;

    @Column(name = "owner_department")
    @Enumerated(EnumType.STRING)
    private ToolDepartment ownerDepartment;

    @Enumerated(EnumType.STRING)
    private ToolStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}