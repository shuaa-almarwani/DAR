package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.BillDtoIn;
import com.example.DAR.DTO.Out.BillComparisonDtoOut;
import com.example.DAR.DTO.Out.BillComparisonResponseDtoOut;
import com.example.DAR.DTO.Out.BillDtoOut;
import com.example.DAR.DTO.Out.BillMonthlyReportDtoOut;
import com.example.DAR.Model.Bill;
import com.example.DAR.Model.Home;
import com.example.DAR.Repository.BillRepository;
import com.example.DAR.Repository.HomeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Billservice {
    private final BillRepository billRepository;
    private final HomeRepository homeRepository;
    private final ModelMapper modelMapper;
    private final AiService aiService;
    private final NotificationService notificationService;

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
        checkAndFlagAnomaly(bill, home);
    }

    private void checkAndFlagAnomaly(Bill bill, Home home) {
        List<Bill> previous = billRepository.findTop3ByHomeIdAndTypeOrderByBillMonthDesc(home.getId(), bill.getType());
        previous = previous.stream().filter(b -> !b.getId().equals(bill.getId())).toList();
        if (previous.size() < 2) return;

        double avg = previous.stream().mapToInt(Bill::getConsumption).average().orElse(0);
        if (avg > 0 && bill.getConsumption() > avg * 1.3) {
            bill.setIsAnomaly(true);
            billRepository.save(bill);
            String aiExplanation = aiService.generateAnomalyExplanation(bill.getType(), bill.getConsumption(), avg, bill.getUnit());
            notificationService.sendBillAnomalyNotification(home.getUser(), bill.getType(), bill.getConsumption(), avg, aiExplanation);
        }
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
        if (bill == null) throw new ApiException("bill not found");
        bill.setStatus("PAID");
        billRepository.save(bill);
    }

    // MARK AS OVERDUE
    public void markAsOverdue(Integer id) {
        Bill bill = billRepository.findBillById(id);
        if (bill == null) throw new ApiException("bill not found");
        bill.setStatus("OVERDUE");
        billRepository.save(bill);
    }

    // GET by Status
    public List<BillDtoOut> getBillsByStatus(Integer homeId, String status) {
        if (homeRepository.findHomeById(homeId) == null) throw new ApiException("home not found");
        return billRepository.findByHomeIdAndStatus(homeId, status.toUpperCase())
                .stream().map(b -> modelMapper.map(b, BillDtoOut.class)).toList();
    }

    // UPLOAD IMAGE → AI extract → save Bill
    public BillDtoOut addBillFromImage(Integer homeId, MultipartFile file) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) throw new ApiException("home not found");

        String json = aiService.extractBillDataFromImage(file);
        try {
            Map<String, Object> data = new ObjectMapper().readValue(json, Map.class);
            Bill bill = new Bill();
            bill.setHome(home);
            bill.setType(((String) data.get("type")).toUpperCase());
            bill.setBillMonth(LocalDate.parse((String) data.get("billMonth")));
            bill.setConsumption(((Number) data.get("consumption")).intValue());
            bill.setAmount(((Number) data.get("amount")).doubleValue());
            bill.setUnit((String) data.get("unit"));
            bill.setIsInstallment((Boolean) data.getOrDefault("isInstallment", false));
            bill.setTotalInstallment(((Number) data.getOrDefault("totalInstallment", 0)).intValue());
            bill.setPaidInstallment(((Number) data.getOrDefault("paidInstallment", 0)).intValue());
            bill.setStatus((String) data.getOrDefault("status", "PENDING"));
            bill.setIsAnomaly(false);
            bill.setImageUrl((String) data.getOrDefault("imageUrl", ""));
            billRepository.save(bill);
            checkAndFlagAnomaly(bill, home);
            return modelMapper.map(bill, BillDtoOut.class);
        } catch (Exception e) {
            throw new ApiException("Failed to parse AI response: " + e.getMessage());
        }
    }

    // COMPARE BILLS
    public BillComparisonResponseDtoOut compareBills(Integer homeId, String type, int months) {
        if (homeRepository.findHomeById(homeId) == null) throw new ApiException("home not found");
        LocalDate from = LocalDate.now().minusMonths(months).withDayOfMonth(1);
        List<Bill> bills = billRepository.findByHomeIdAndTypeAndBillMonthAfter(homeId, type.toUpperCase(), from);

        List<BillComparisonDtoOut> data = bills.stream()
                .map(b -> new BillComparisonDtoOut(
                        b.getBillMonth().getYear() + "-" + String.format("%02d", b.getBillMonth().getMonthValue()),
                        b.getConsumption(),
                        b.getAmount()))
                .toList();

        String monthlyData = data.stream()
                .map(d -> d.getMonth() + ": " + d.getConsumption() + " units, " + d.getAmount() + " SAR")
                .collect(java.util.stream.Collectors.joining("\n"));

        String aiNote = data.size() < 2 ? "Not enough data to analyze trends." :
                aiService.generateBillComparisonNote(type, monthlyData);

        return new BillComparisonResponseDtoOut(type.toUpperCase(), data, aiNote);
    }

    // MONTHLY REPORT
    public List<BillMonthlyReportDtoOut> getMonthlyReport(Integer homeId, int year, int month) {
        if (homeRepository.findHomeById(homeId) == null) throw new ApiException("home not found");
        List<Bill> bills = billRepository.findByHomeIdAndYearAndMonth(homeId, year, month);
        Map<String, BillMonthlyReportDtoOut> reportMap = new java.util.LinkedHashMap<>();
        for (Bill b : bills) {
            reportMap.computeIfAbsent(b.getType(), t -> new BillMonthlyReportDtoOut(t, 0, 0.0, 0, null));
            BillMonthlyReportDtoOut r = reportMap.get(b.getType());
            r.setTotalConsumption(r.getTotalConsumption() + b.getConsumption());
            r.setTotalAmount(r.getTotalAmount() + (b.getAmount() != null ? b.getAmount() : 0));
            r.setBillCount(r.getBillCount() + 1);
        }
        return new java.util.ArrayList<>(reportMap.values());
    }

    // GET Anomalies by Home
    public List<BillDtoOut> getAnomalyBills(Integer homeId) {
        if (homeRepository.findHomeById(homeId) == null) throw new ApiException("home not found");
        return billRepository.findByHomeIdAndIsAnomalyTrue(homeId)
                .stream().map(b -> modelMapper.map(b, BillDtoOut.class)).toList();
    }

    // DELETE
    public void deleteBill(Integer id) {
        if (!billRepository.existsById(id)) {
            throw new ApiException("bill not found");
        }
        billRepository.deleteById(id);
    }
    public Map<String, ? > currentMonthElectricity(Integer homeId){
        if (homeRepository.findHomeById(homeId) == null) throw new ApiException("home not found");
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        Double amount = billRepository.sumAmountByHomeIdAndTypeAndMonth(homeId, "ELECTRICITY", year, month);
        return Map.of("type", "ELECTRICITY", "amount", amount != null ? amount : 0.0, "period", year + "-" + String.format("%02d", month));
    }
    public Map<String, ? > currentMonthWater(Integer homeId){
        if (homeRepository.findHomeById(homeId) == null) throw new ApiException("home not found");
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        Double amount = billRepository.sumAmountByHomeIdAndTypeAndMonth(homeId, "WATER", year, month);
        return Map.of("type", "WATER", "amount", amount != null ? amount : 0.0, "period", year + "-" + String.format("%02d", month));}

    public Map<String, ? > currentMonthGas(Integer homeId){
        if (homeRepository.findHomeById(homeId) == null) throw new ApiException("home not found");
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        Double amount = billRepository.sumAmountByHomeIdAndTypeAndMonth(homeId, "GAS", year, month);
        return Map.of("type", "GAS", "amount", amount != null ? amount : 0.0, "period", year + "-" + String.format("%02d", month));}

    public Map<String, ? > anomaliesCount(Integer homeId) {
        if (homeRepository.findHomeById(homeId) == null) throw new ApiException("home not found");
        Long count = billRepository.countAnomaliesByHomeId(homeId);
        return Map.of("anomaliesCount", count != null ? count : 0);
    }

}
