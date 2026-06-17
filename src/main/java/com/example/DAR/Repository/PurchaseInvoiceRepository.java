package com.example.DAR.Repository;

import com.example.DAR.Model.PurchaseInvoice;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseInvoiceRepository extends JpaRepository<PurchaseInvoice,Integer> {
    PurchaseInvoice findPurchaseInvoiceById(Integer id);

    List<PurchaseInvoice> findPurchaseInvoiceByHomeId(Integer homeId);

    List<PurchaseInvoice> findPurchaseInvoiceByHomeIdAndCategory(Integer homeId, String category);
}
