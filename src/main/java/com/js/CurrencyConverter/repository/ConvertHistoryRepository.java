package com.js.CurrencyConverter.repository;

import com.js.CurrencyConverter.entity.ConvertHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ConvertHistoryRepository extends JpaRepository<ConvertHistory, Long> {
    List<ConvertHistory> findAllByUserToken(String userToken);

//    @Modifying
//    @Query("delete from convert_history ch where ch.user_token = :userToken")
    @Transactional
    void deleteAllByUserToken(String userToken);
}
