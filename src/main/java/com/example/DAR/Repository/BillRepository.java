package com.example.DAR.Repository;

import com.example.DAR.Model.Bill;
import com.example.DAR.Model.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    Bill findBillById(Integer id);
    List<Bill> findBillByHome(Home home);
    List<Bill> findByHomeIdAndType(Integer homeId, String type);
    List<Bill> findTop3ByHomeIdAndTypeOrderByBillMonthDesc(Integer homeId, String type);
    List<Bill> findByHomeIdAndIsAnomalyTrue(Integer homeId);

    @Query("SELECT b FROM Bill b WHERE b.home.id = :homeId AND YEAR(b.billMonth) = :year AND MONTH(b.billMonth) = :month")
    List<Bill> findByHomeIdAndYearAndMonth(@Param("homeId") Integer homeId, @Param("year") int year, @Param("month") int month);

    List<Bill> findByDueDateBetweenAndStatus(LocalDate from, LocalDate to, String status);
    List<Bill> findByHomeIdAndStatus(Integer homeId, String status);

    @Query("SELECT SUM(b.amount) FROM Bill b WHERE b.home.id = :homeId AND b.type = :type AND YEAR(b.billMonth) = :year AND MONTH(b.billMonth) = :month")
    Double sumAmountByHomeIdAndTypeAndMonth(@Param("homeId") Integer homeId, @Param("type") String type, @Param("year") int year, @Param("month") int month);

    @Query("SELECT COUNT(b) FROM Bill b WHERE b.home.id = :homeId AND b.isAnomaly = true")
    Long countAnomaliesByHomeId(@Param("homeId") Integer homeId);

    @Query("SELECT b FROM Bill b WHERE b.home.id = :homeId AND b.type = :type AND b.billMonth >= :from ORDER BY b.billMonth ASC")
    List<Bill> findByHomeIdAndTypeAndBillMonthAfter(@Param("homeId") Integer homeId, @Param("type") String type, @Param("from") LocalDate from);
}
