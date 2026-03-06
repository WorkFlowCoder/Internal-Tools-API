package com.techcorp.management.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import org.hibernate.validator.constraints.URL;
import jakarta.validation.constraints.*;

import com.fasterxml.jackson.annotation.*;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tools")
public class Tool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Nom entre 2 et 100 caractères")
    @Column(unique = true)
    private String name;

    private String description;

    @NotBlank(message = "Vndeur obligatoire")
    @Size(max = 100, message = "100 caractères maximum")
    private String vendor;

    @URL(message = "Format d'URL invalide")
    @Column(name = "website_url")
    private String websiteUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties("category")
    private Category category;

    @NotNull(message = "Cout mensuel obligatoire")
    @DecimalMin(value = "0.0", inclusive = true, message = "Cout >= 0")
    @Column(name = "monthly_cost", columnDefinition = "DOUBLE")
    private Double monthlyCost;

    @Column(name = "active_users_count", nullable = false)
    private Integer activeUsersCount = 0;

    @NotNull(message = "Departement obligatoire")
    @Column(name = "owner_department")
    @Enumerated(EnumType.STRING)
    private Department ownerDepartment;

    @Enumerated(EnumType.STRING)
    private ToolStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "tool", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UsageLog> usageLogs;

    @OneToMany(mappedBy = "tool", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CostTracking> costTrackings;

    @OneToMany(mappedBy = "tool", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AccessRequest> accessRequests;

    @OneToMany(mappedBy = "tool", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserToolAccess> userToolAccess;

    @JsonProperty("category_id")
    public void setCategoryFromId(Long id) {
        if (id != null) {
            Category cat = new Category();
            cat.setId(id);
            this.category = cat;
        }
    }
}