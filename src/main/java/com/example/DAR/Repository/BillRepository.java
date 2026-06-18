package com.example.DAR.Repository;

import com.example.DAR.Model.Bill;
import com.example.DAR.Model.Home;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    Bill findBillById(Integer id);
    List<Bill> findBillByHome(Home home);
    List<Bill> findByHomeIdAndType(Integer homeId, String type);
    List<Bill> findTop3ByHomeIdAndTypeOrderByBillMonthDesc(Integer homeId, String type);
    List<Bill> findByHomeIdAndIsAnomalyTrue(Integer homeId);
}
