package com.example.DAR.Service;


import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.PurchaseInvoiceDtoIn;
import com.example.DAR.DTO.Out.PurchaseInvoiceDtoOut;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.PurchaseInvoice;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.PurchaseInvoiceRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseInvoiceService {
    private final PurchaseInvoiceRepository purchaseInvoiceRepository;
    private final ModelMapper modelMapper;
    private final HomeRepository homeRepository ;

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
    //4 extra endpoint

}
