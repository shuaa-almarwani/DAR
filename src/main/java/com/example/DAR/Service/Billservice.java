package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.BillDtoIn;
import com.example.DAR.DTO.Out.BillDtoOut;
import com.example.DAR.DTO.Out.SensorDtoOut;
import com.example.DAR.Model.Bill;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.Sensor;
import com.example.DAR.Repository.BillRepository;
import com.example.DAR.Repository.HomeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Billservice {
    private final BillRepository billRepository;
    private final HomeRepository homeRepository;
    private final ModelMapper modelMapper;

    // CREATE
    public void addBill(Integer homeId, BillDtoIn dto) {
        Home home = homeRepository.findHomeById(homeId);
        if (home==null) {
            throw new ApiException("home not found");
        }
        Bill bill = modelMapper.map(dto, Bill.class);
        bill.setHome(home);
        bill.setStatus("PENDING");
        bill.setIsAnomaly(false);

         billRepository.save(bill);
    }

    // GET ALL by Home
    public List<BillDtoOut> getAllBillsByHome(Integer homeId) {
        Home home = homeRepository.findHomeById(homeId);
        if (home==null) {
            throw new ApiException("home not found");
        }
        List<Bill> bill = billRepository.findBillByHome(home);
        return bill.stream().map(b -> modelMapper.map(b, BillDtoOut.class)).toList();

    }

    // GET ONE
    public BillDtoOut getBillById(Integer id) {
        Bill bill = billRepository.findBillById(id);
        if (bill==null) {
            throw new ApiException("bill not found");
        }
        return modelMapper.map(bill, BillDtoOut.class);
    }

    // GET by Type
    public List<BillDtoOut> getBillsByType(Integer homeId, String type) {
        Home home = homeRepository.findHomeById(homeId);
        if (home==null) {
            throw new ApiException("home not found");
        }
        List<Bill> bill = billRepository.findByHomeIdAndType(homeId, type);
        return bill.stream().map(b -> modelMapper.map(b, BillDtoOut.class)).toList();


    }


    // UPDATE
    public void updateBill(Integer id, BillDtoIn dto) {
        Bill existing = billRepository.findBillById(id);
        if (existing==null) {
            throw new ApiException("bill not found");
        }
        modelMapper.map(dto, existing);
        billRepository.save(existing);
    }

    // MARK AS PAID
    public void markAsPaid(Integer id) {
        Bill bill = billRepository.findBillById(id);
        if (bill==null) {
            throw new ApiException("bill not found");
        }
        bill.setStatus("PAID");
        billRepository.save(bill);
    }

    // DELETE
    public void deleteBill(Integer id) {
        if (!billRepository.existsById(id)) {
            throw new ApiException("bill not found");
        }
        billRepository.deleteById(id);
    }
}
