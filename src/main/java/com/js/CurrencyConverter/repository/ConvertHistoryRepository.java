package com.js.CurrencyConverter.repository;

import com.js.CurrencyConverter.entity.ConvertHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvertHistoryRepository extends JpaRepository<ConvertHistory, Long> {
}
