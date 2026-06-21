package com.example.DAR.Repository;

import com.example.DAR.Model.PurchaseInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PurchaseInvoiceRepository extends JpaRepository<PurchaseInvoice,Integer> {
    PurchaseInvoice findPurchaseInvoiceById(Integer id);
    List<PurchaseInvoice> findPurchaseInvoiceByHomeId(Integer homeId);
    List<PurchaseInvoice> findPurchaseInvoiceByHomeIdAndCategory(Integer homeId, String category);

    @Query("SELECT p.category, SUM(p.amount), COUNT(p) FROM PurchaseInvoice p WHERE p.home.id = :homeId GROUP BY p.category ORDER BY SUM(p.amount) DESC")
    List<Object[]> findStatsByHomeId(@Param("homeId") Integer homeId);

    @Query("SELECT p FROM PurchaseInvoice p WHERE p.home.id = :homeId ORDER BY p.amount DESC")
    List<PurchaseInvoice> findTopPurchasesByHomeId(@Param("homeId") Integer homeId);

    List<PurchaseInvoice> findByWarrantyExpiryBetween(LocalDate from, LocalDate to);

    @Query("SELECT COUNT(p) FROM PurchaseInvoice p WHERE p.home.id = :homeId AND p.warrantyExpiry >= :today")
    Long countActiveWarrantiesByHomeId(@Param("homeId") Integer homeId, @Param("today") LocalDate today);
}
