package com.kt.setly.settlement.repository;

import com.kt.setly.settlement.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    List<Settlement> findByGroup_IdOrderBySettlementDateDescIdDesc(Long groupId);
}
