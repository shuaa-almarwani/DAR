package com.example.DAR;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.BillDtoIn;
import com.example.DAR.Model.Bill;
import com.example.DAR.Model.Home;
import com.example.DAR.Repository.BillRepository;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Service.Billservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BillserviceTest {

    @Mock private BillRepository billRepository;
    @Mock private HomeRepository homeRepository;


    @InjectMocks
    private Billservice billservice;

    private Home home;
    private Bill bill;

    @BeforeEach
    void setUp() {
        home = new Home();
        home.setId(1);

        bill = new Bill();
        bill.setId(1);
        bill.setType("ELECTRICITY");
        bill.setConsumption(200);
        bill.setAmount(500.0);
        bill.setStatus("PENDING");
        bill.setIsAnomaly(false);
        bill.setHome(home);
    }

    @Test
    @DisplayName("Should throw exception when home not found on addBill")
    void addBill_homeNotFound_throwsException() {
        when(homeRepository.findHomeById(99)).thenReturn(null);

        assertThrows(ApiException.class, () -> billservice.addBill(99, new BillDtoIn()));
    }

    @Test
    @DisplayName("Should mark bill as paid successfully")
    void markAsPaid_success() {
        when(billRepository.findBillById(1)).thenReturn(bill);

        billservice.markAsPaid(1);

        assertEquals("PAID", bill.getStatus());
        verify(billRepository).save(bill);
    }

    @Test
    @DisplayName("Should throw exception when bill not found on deleteBill")
    void deleteBill_notFound_throwsException() {
        when(billRepository.existsById(99)).thenReturn(false);

        assertThrows(ApiException.class, () -> billservice.deleteBill(99));
    }
}
