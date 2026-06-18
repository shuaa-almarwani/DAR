package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.PurchaseInvoiceDtoIn;
import com.example.DAR.Service.PurchaseInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/purchase-invoice")
@RequiredArgsConstructor
public class PurchaseInvoiceController {

    private final PurchaseInvoiceService purchaseInvoiceService;

    @PostMapping("/add/{homeId}")
    public ResponseEntity<?> addInvoice(@PathVariable Integer homeId, @RequestBody @Valid PurchaseInvoiceDtoIn dto) {
        purchaseInvoiceService.addPurchaseInvoice(homeId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Invoice added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable Integer id, @RequestBody @Valid PurchaseInvoiceDtoIn dto) {
        purchaseInvoiceService.UpdatePurchaseInvoice(id, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Invoice updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable Integer id) {
        purchaseInvoiceService.deletePurchaseInvoice(id);
        return ResponseEntity.status(200).body(new ApiResponse("Invoice deleted successfully"));
    }

    @GetMapping("/get/home/{homeId}")
    public ResponseEntity<?> getAllInvoicesByHome(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(purchaseInvoiceService.getAllInvoicesByHome(homeId));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getInvoiceById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(purchaseInvoiceService.getInvoiceById(id));
    }

    @GetMapping("/get/category/{homeId}/{category}")
    public ResponseEntity<?> getInvoicesByCategory(@PathVariable Integer homeId, @PathVariable String category) {
        return ResponseEntity.status(200).body(purchaseInvoiceService.getInvoicesByCategory(homeId, category));
    }

    @PostMapping("/upload/{homeId}")
    public ResponseEntity<?> addInvoiceFromImage(@PathVariable Integer homeId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(200).body(purchaseInvoiceService.addInvoiceFromImage(homeId, file));
    }
}
