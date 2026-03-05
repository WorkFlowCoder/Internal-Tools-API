package com.techcorp.management.repository;

import com.techcorp.management.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    
    List<Tool> findByOwnerDepartementAndStatus(String department, String status);

    List<Tool> findByMonthlyCostBetweenAndCategoryId(Double min, Double max, Integer categoryId);
}