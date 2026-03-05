package com.techcorp.management.repository;

import com.techcorp.management.entity.Tool;
import com.techcorp.management.entity.Category;
import com.techcorp.management.entity.ToolStatus;
import com.techcorp.management.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long>, JpaSpecificationExecutor<Tool> {
    
    List<Tool> findByOwnerDepartmentAndStatus(Department department, ToolStatus status);

    List<Tool> findByMonthlyCostBetweenAndCategory(Double min, Double max, Category category);

    boolean existsByName(String name);
}