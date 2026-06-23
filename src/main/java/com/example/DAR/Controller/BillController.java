package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.BillDtoIn;
import com.example.DAR.Service.Billservice;
import com.example.DAR.Service.PdfReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/bill")
@RequiredArgsConstructor
public class BillController {

    private final Billservice billService;
    private final PdfReportService pdfReportService;

    @PostMapping("/add/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> addBill(@PathVariable Integer homeId, @RequestBody @Valid BillDtoIn dto) {
        billService.addBill(homeId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Bill added successfully"));
    }

    @GetMapping("/get/home/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> getAllBillsByHome(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(billService.getAllBillsByHome(homeId));
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserBill(#id)")
    public ResponseEntity<?> getBillById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(billService.getBillById(id));
    }

    @GetMapping("/get/type/{homeId}/{type}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> getBillsByType(@PathVariable Integer homeId, @PathVariable String type) {
        return ResponseEntity.status(200).body(billService.getBillsByType(homeId, type));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserBill(#id)")
    public ResponseEntity<?> updateBill(@PathVariable Integer id, @RequestBody @Valid BillDtoIn dto) {
        billService.updateBill(id, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Bill updated successfully"));
    }

    @PutMapping("/pay/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserBill(#id)")
    public ResponseEntity<?> markAsPaid(@PathVariable Integer id) {
        billService.markAsPaid(id);
        return ResponseEntity.status(200).body(new ApiResponse("Bill marked as paid"));
    }

    @PutMapping("/overdue/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserBill(#id)")
    public ResponseEntity<?> markAsOverdue(@PathVariable Integer id) {
        billService.markAsOverdue(id);
        return ResponseEntity.status(200).body(new ApiResponse("Bill marked as overdue"));
    }

    @GetMapping("/get/status/{homeId}/{status}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> getBillsByStatus(@PathVariable Integer homeId, @PathVariable String status) {
        return ResponseEntity.status(200).body(billService.getBillsByStatus(homeId, status));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserBill(#id)")
    public ResponseEntity<?> deleteBill(@PathVariable Integer id) {
        billService.deleteBill(id);
        return ResponseEntity.status(200).body(new ApiResponse("Bill deleted successfully"));
    }

    @PostMapping("/upload/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> addBillFromImage(@PathVariable Integer homeId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(200).body(billService.addBillFromImage(homeId, file));
    }

    @GetMapping("/get/anomalies/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> getAnomalyBills(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(billService.getAnomalyBills(homeId));
    }

    @GetMapping("/report/{homeId}/{year}/{month}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> getMonthlyReport(@PathVariable Integer homeId, @PathVariable int year, @PathVariable int month) {
        return ResponseEntity.status(200).body(billService.getMonthlyReport(homeId, year, month));
    }

    @GetMapping("/compare/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> compareBills(@PathVariable Integer homeId,
                                          @RequestParam String type,
                                          @RequestParam(defaultValue = "6") int months) {
        return ResponseEntity.status(200).body(billService.compareBills(homeId, type, months));
    }


    @GetMapping("/report/pdf/{homeId}/{year}/{month}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<byte[]> getMonthlyReportPdf(@PathVariable Integer homeId, @PathVariable int year, @PathVariable int month) {
        byte[] pdf = pdfReportService.generateMonthlyReport(homeId, year, month);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report-" + year + "-" + month + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    // كهرباء الشهر
    @GetMapping("/electricity/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> currentMonthElectricity(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(billService.currentMonthElectricity(homeId));
    }

    // مياه الشهر
    @GetMapping("/water/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> currentMonthWater(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(billService.currentMonthWater(homeId));
    }
    @GetMapping("/gas/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> currentMonthGas(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(billService.currentMonthGas(homeId));
    }
    // تنبيهات استهلاك
    @GetMapping("/anomalies-count/{homeId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserHome(#homeId)")
    public ResponseEntity<?> anomaliesCount(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(billService.anomaliesCount(homeId));

    }
}
