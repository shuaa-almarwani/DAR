package com.example.DAR.Service;


import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.PurchaseInvoiceDtoIn;
import com.example.DAR.DTO.Out.PurchaseInvoiceDtoOut;
import com.example.DAR.DTO.Out.PurchaseInvoiceStatsDtoOut;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.PurchaseInvoice;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.PurchaseInvoiceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PurchaseInvoiceService {
    private final PurchaseInvoiceRepository purchaseInvoiceRepository;
    private final ModelMapper modelMapper;
    private final HomeRepository homeRepository;
    private final AiService aiService;

    public void addPurchaseInvoice(Integer homeId, PurchaseInvoiceDtoIn dto){
        Home home =homeRepository.findHomeById(homeId);
        if (home == null) throw new ApiException("home not found");
        PurchaseInvoice invoice = modelMapper.map(dto, PurchaseInvoice.class);
        invoice.setHome(home);
        purchaseInvoiceRepository.save(invoice);
    }
    public void UpdatePurchaseInvoice (Integer id, PurchaseInvoiceDtoIn invoiceIN){
        PurchaseInvoice existing = purchaseInvoiceRepository.findPurchaseInvoiceById(id);
        if (existing == null) throw new ApiException("invoice not found");
        existing = modelMapper.map(invoiceIN, PurchaseInvoice.class);
        purchaseInvoiceRepository.save(existing);
    }
    public void deletePurchaseInvoice  (Integer id){
        PurchaseInvoice invoice = purchaseInvoiceRepository.findPurchaseInvoiceById(id);
        if (invoice == null) throw new ApiException("invoice not found");
        purchaseInvoiceRepository.delete(invoice);
    }
    // GET ALL by Home
    public List<PurchaseInvoiceDtoOut> getAllInvoicesByHome(Integer homeId) {
        Home home =homeRepository.findHomeById(homeId);
        if (home == null) throw new ApiException("home not found");
        List<PurchaseInvoice> invoices = purchaseInvoiceRepository.findPurchaseInvoiceByHomeId(homeId);
        return invoices.stream().map(invoice -> modelMapper.map(invoice, PurchaseInvoiceDtoOut.class)).toList();
    }

    // GET ONE
    public PurchaseInvoiceDtoOut getInvoiceById(Integer id) {
        PurchaseInvoice invoice = purchaseInvoiceRepository.findPurchaseInvoiceById(id);
        if (invoice == null) throw new ApiException("invoice not found");
        return modelMapper.map(invoice, PurchaseInvoiceDtoOut.class) ;
    }

    // GET by Category
    public List<PurchaseInvoiceDtoOut> getInvoicesByCategory(Integer homeId, String category) {
        Home home =homeRepository.findHomeById(homeId);
        if (home == null) throw new ApiException("home not found");
        List<PurchaseInvoice> invoices = purchaseInvoiceRepository.findPurchaseInvoiceByHomeIdAndCategory(homeId ,category);
        return invoices.stream().map(invoice -> modelMapper.map(invoice, PurchaseInvoiceDtoOut.class)).toList();
    }
    // STATS by Home
    // Groups purchases by category for spending statistics.
    public PurchaseInvoiceStatsDtoOut getStatsByHome(Integer homeId) {
        if (homeRepository.findHomeById(homeId) == null) throw new ApiException("home not found");

        List<Object[]> rawStats = purchaseInvoiceRepository.findStatsByHomeId(homeId);
        List<PurchaseInvoiceStatsDtoOut.CategoryStatDtoOut> byCategory = rawStats.stream()
                .map(row -> new PurchaseInvoiceStatsDtoOut.CategoryStatDtoOut(
                        (String) row[0],
                        ((Number) row[1]).doubleValue(),
                        ((Number) row[2]).longValue()))
                .toList();

        List<PurchaseInvoiceDtoOut> topPurchases = purchaseInvoiceRepository.findTopPurchasesByHomeId(homeId)
                .stream().limit(5)
                .map(p -> modelMapper.map(p, PurchaseInvoiceDtoOut.class))
                .toList();

        return new PurchaseInvoiceStatsDtoOut(byCategory, topPurchases);
    }

    // UPLOAD IMAGE → AI extract → save Invoice
    // Uses AI image extraction, then saves the invoice normally.
    public PurchaseInvoiceDtoOut addInvoiceFromImage(Integer homeId, MultipartFile file) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) throw new ApiException("home not found");

        String json = aiService.extractPurchaseInvoiceDataFromImage(file);
        try {
            Map<String, Object> data = new ObjectMapper().readValue(json, Map.class);
            PurchaseInvoice invoice = new PurchaseInvoice();
            invoice.setHome(home);
            invoice.setProductName((String) data.get("productName"));
            invoice.setStore((String) data.get("store"));
            invoice.setAmount(((Number) data.get("amount")).doubleValue());
            invoice.setPurchaseDate(LocalDate.parse((String) data.get("purchaseDate")));
            invoice.setCategory((String) data.get("category"));
            invoice.setImageUrl((String) data.getOrDefault("imageUrl", ""));
            invoice.setWarrantyNote((String) data.getOrDefault("warrantyNote", null));
            String warrantyExpiry = (String) data.getOrDefault("warrantyExpiry", null);
            invoice.setWarrantyExpiry(warrantyExpiry != null ? LocalDate.parse(warrantyExpiry) : null);
            purchaseInvoiceRepository.save(invoice);
            return modelMapper.map(invoice, PurchaseInvoiceDtoOut.class);
        } catch (Exception e) {
            throw new ApiException("Failed to parse AI response: " + e.getMessage());
        }

    }
    // Counts warranties that have not expired yet.
    public  Map<String, ? > countActiveWarranties(Integer homeId){
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) throw new ApiException("home not found");
        Long count = purchaseInvoiceRepository.countActiveWarrantiesByHomeId(homeId, LocalDate.now());
       return Map.of("activeWarranties", count != null ? count : 0);
 }
}
