package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.BillDtoIn;
import com.example.DAR.Service.Billservice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bill")
@RequiredArgsConstructor
public class BillController {

    private final Billservice billService;

    @PostMapping("/add/{homeId}")
    public ResponseEntity<?> addBill(@PathVariable Integer homeId, @RequestBody @Valid BillDtoIn dto) {
        billService.addBill(homeId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Bill added successfully"));
    }

    @GetMapping("/get/home/{homeId}")
    public ResponseEntity<?> getAllBillsByHome(@PathVariable Integer homeId) {
        return ResponseEntity.status(200).body(billService.getAllBillsByHome(homeId));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getBillById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(billService.getBillById(id));
    }

    @GetMapping("/get/type/{homeId}/{type}")
    public ResponseEntity<?> getBillsByType(@PathVariable Integer homeId, @PathVariable String type) {
        return ResponseEntity.status(200).body(billService.getBillsByType(homeId, type));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBill(@PathVariable Integer id, @RequestBody @Valid BillDtoIn dto) {
        billService.updateBill(id, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Bill updated successfully"));
    }

    @PutMapping("/pay/{id}")
    public ResponseEntity<?> markAsPaid(@PathVariable Integer id) {
        billService.markAsPaid(id);
        return ResponseEntity.status(200).body(new ApiResponse("Bill marked as paid"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBill(@PathVariable Integer id) {
        billService.deleteBill(id);
        return ResponseEntity.status(200).body(new ApiResponse("Bill deleted successfully"));
    }
}
