package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.Out.BillMonthlyReportDtoOut;
import com.example.DAR.DTO.Out.PurchaseInvoiceStatsDtoOut;
import com.example.DAR.DTO.Out.SensorReportDtoOut;
import com.example.DAR.Model.Home;
import com.example.DAR.Repository.HomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PdfReportService {

    private final HomeRepository homeRepository;
    private final Billservice billService;
    private final PurchaseInvoiceService purchaseInvoiceService;
    private final Sensorreadingservice sensorReadingService;
    private final AiService aiService;

    public byte[] generateMonthlyReport(Integer homeId, int year, int month) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) throw new ApiException("home not found");

        List<BillMonthlyReportDtoOut> bills = billService.getMonthlyReport(homeId, year, month);
        PurchaseInvoiceStatsDtoOut stats = purchaseInvoiceService.getStatsByHome(homeId);

        String billsData = bills.isEmpty() ? "No bills found" :
                bills.stream().map(b -> translateType(b.getType()) + ": consumption=" + b.getTotalConsumption() + ", amount=" + b.getTotalAmount() + " SAR")
                        .collect(Collectors.joining("\n"));

        String purchasesData = stats.getByCategory().isEmpty() ? "No purchases found" :
                stats.getByCategory().stream().map(c -> c.getCategory() + ": " + c.getTotalAmount() + " SAR (" + c.getCount() + " invoices)")
                        .collect(Collectors.joining("\n"));

        String aiSummary = aiService.generateMonthlyReportSummary(home.getAddress(), year, month, billsData, purchasesData);

        String html = buildHtml(home.getAddress(), year, month, bills, stats, aiSummary);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new ApiException("Failed to generate PDF: " + e.getMessage());
        }
    }

    private String buildHtml(String address, int year, int month,
                              List<BillMonthlyReportDtoOut> bills,
                              PurchaseInvoiceStatsDtoOut stats,
                              String aiSummary) {

        StringBuilder billRows = new StringBuilder();
        for (BillMonthlyReportDtoOut b : bills) {
            billRows.append("<tr>")
                    .append("<td style=\"border:1px solid #E2CFC3; padding:8px;\">").append(translateType(b.getType())).append("</td>")
                    .append("<td style=\"border:1px solid #E2CFC3; padding:8px;\">").append(b.getTotalConsumption()).append("</td>")
                    .append("<td style=\"border:1px solid #E2CFC3; padding:8px;\">").append(b.getTotalAmount()).append(" SAR</td>")
                    .append("<td style=\"border:1px solid #E2CFC3; padding:8px;\">").append(b.getBillCount()).append("</td>")
                    .append("<td style=\"border:1px solid #E2CFC3; padding:8px;\">").append(translateStatus(b.getStatus())).append("</td>")
                    .append("</tr>");
        }

        if (bills.isEmpty()) {
            billRows.append("<tr><td colspan=\"4\" style=\"border:1px solid #E2CFC3; padding:8px; text-align:center; color:#A68972;\">No bills found for this period</td></tr>");
        }

        StringBuilder categoryRows = new StringBuilder();
        for (PurchaseInvoiceStatsDtoOut.CategoryStatDtoOut c : stats.getByCategory()) {
            categoryRows.append("<tr>")
                    .append("<td style=\"border:1px solid #E2CFC3; padding:8px;\">").append(c.getCategory()).append("</td>")
                    .append("<td style=\"border:1px solid #E2CFC3; padding:8px;\">").append(c.getTotalAmount()).append(" SAR</td>")
                    .append("<td style=\"border:1px solid #E2CFC3; padding:8px;\">").append(c.getCount()).append("</td>")
                    .append("</tr>");
        }

        if (stats.getByCategory().isEmpty()) {
            categoryRows.append("<tr><td colspan=\"3\" style=\"border:1px solid #E2CFC3; padding:8px; text-align:center; color:#A68972;\">No purchases found</td></tr>");
        }

        String aiHtml = aiSummary
                .replaceAll("\\*\\*(.+?)\\*\\*", "<b>$1</b>")
                .replaceAll("(?m)^(Consumption Summary:|Expenses Summary:|Observations:|Recommendations:)", "<b style=\"color:#765345;\">$1</b>")
                .replaceAll("(?m)^- (.+)$", "<span style=\"color:#765345;\">&#8226;</span> $1")
                .replace("---", "")
                .replace("\n\n", "</p><p style=\"margin:8px 0;\">")
                .replace("\n", "<br/>");

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
               "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
               "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
               "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head>" +
               "<body style=\"margin:0; padding:0; background-color:#E8DED2; font-family:Arial,sans-serif;\">" +
               "<div style=\"max-width:680px; margin:auto; padding:34px 18px;\">" +
               "<div style=\"background:linear-gradient(135deg,#FFF8F4 0%,#F7E8E1 55%,#E8DED2 100%); border-radius:24px; overflow:hidden; border:1px solid #E2CFC3;\">" +

               // Header
               "<div style=\"padding:24px 28px 18px; border-bottom:1px solid rgba(166,137,114,0.22);\">" +
               "<p style=\"margin:0 0 8px; display:inline-block; background-color:#F3DCD2; color:#765345; padding:7px 14px; border-radius:999px; font-size:13px;\">Monthly Report - DAR Platform</p>" +
               "<h1 style=\"margin:8px 0 4px; color:#765345; font-size:28px; font-weight:800;\">DAR</h1>" +
               "<p style=\"margin:0; color:#A68972; font-size:13px;\">Smart Home Management</p>" +
               "<p style=\"margin:8px 0 0; color:#3E302A; font-size:13px;\"><b>Home:</b> " + address + " &nbsp; <b>Period:</b> " + month + "/" + year + "</p>" +
               "</div>" +

               // Bills Table
               "<div style=\"padding:24px 28px 0;\">" +
               "<h2 style=\"color:#765345; font-size:16px; margin:0 0 10px;\">Utility Bills</h2>" +
               "<table style=\"width:100%; border-collapse:collapse;\">" +
               "<tr>" +
               "<th style=\"background-color:#765345; color:white; padding:8px; text-align:left; font-size:13px;\">Type</th>" +
               "<th style=\"background-color:#765345; color:white; padding:8px; text-align:left; font-size:13px;\">Consumption</th>" +
               "<th style=\"background-color:#765345; color:white; padding:8px; text-align:left; font-size:13px;\">Amount</th>" +
               "<th style=\"background-color:#765345; color:white; padding:8px; text-align:left; font-size:13px;\">Count</th>" +
               "<th style=\"background-color:#765345; color:white; padding:8px; text-align:left; font-size:13px;\">Status</th>" +
               "</tr>" +
               billRows +
               "</table>" +
               "</div>" +

               // Purchases Table
               "<div style=\"padding:20px 28px 0;\">" +
               "<h2 style=\"color:#765345; font-size:16px; margin:0 0 10px;\">Purchases by Category</h2>" +
               "<table style=\"width:100%; border-collapse:collapse;\">" +
               "<tr>" +
               "<th style=\"background-color:#765345; color:white; padding:8px; text-align:left; font-size:13px;\">Category</th>" +
               "<th style=\"background-color:#765345; color:white; padding:8px; text-align:left; font-size:13px;\">Total</th>" +
               "<th style=\"background-color:#765345; color:white; padding:8px; text-align:left; font-size:13px;\">Count</th>" +
               "</tr>" +
               categoryRows +
               "</table>" +
               "</div>" +

               // AI Analysis
               "<div style=\"padding:20px 28px;\">" +
               "<h2 style=\"color:#765345; font-size:16px; margin:0 0 10px;\">AI Analysis</h2>" +
               "<div style=\"background-color:rgba(255,255,255,0.72); border:1px solid rgba(166,137,114,0.24); border-radius:18px; padding:18px 20px; font-size:13px; color:#3E302A;\">" +
               "<p style=\"margin:8px 0;\">" + aiHtml + "</p>" +
               "</div>" +
               "</div>" +

               // Footer
               "<div style=\"background-color:#3B241C; padding:18px 28px; text-align:center;\">" +
               "<p style=\"font-size:13px; color:#E8DED2; margin:0;\">DAR Platform - Monthly Report " + month + "/" + year + "</p>" +
               "<p style=\"font-size:12px; color:#A68972; margin:8px 0 0;\">Auto-generated report</p>" +
               "</div>" +

               "</div></div></body></html>";
    }

    public byte[] generateSensorReport(Integer homeId) {
        SensorReportDtoOut report = sensorReadingService.getSensorReport(homeId);

        StringBuilder sensorRows = new StringBuilder();
        for (SensorReportDtoOut.SensorStatDtoOut s : report.getSensors()) {
            String statusColor = Boolean.TRUE.equals(s.getIsActive()) ? "#2e7d32" : "#c62828";
            String statusLabel = Boolean.TRUE.equals(s.getIsActive()) ? "Active" : "Inactive";
            sensorRows.append("<tr>")
                    .append("<td style=\"border:1px solid #E2CFC3; padding:8px;\">").append(s.getType()).append("</td>")
                    .append("<td style=\"border:1px solid #E2CFC3; padding:8px;\">").append(s.getLocation()).append("</td>")
                    .append("<td style=\"border:1px solid #E2CFC3; padding:8px; color:").append(statusColor).append(";\">").append(statusLabel).append("</td>")
                    .append("<td style=\"border:1px solid #E2CFC3; padding:8px;\">").append(s.getReadingCount() != null ? s.getReadingCount() : 0).append("</td>")
                    .append("<td style=\"border:1px solid #E2CFC3; padding:8px;\">").append(s.getAvgValue() != null ? s.getAvgValue() + " " + s.getUnit() : "-").append("</td>")
                    .append("<td style=\"border:1px solid #E2CFC3; padding:8px;\">").append(s.getMinValue() != null ? s.getMinValue() : "-").append("</td>")
                    .append("<td style=\"border:1px solid #E2CFC3; padding:8px;\">").append(s.getMaxValue() != null ? s.getMaxValue() : "-").append("</td>")
                    .append("<td style=\"border:1px solid #E2CFC3; padding:8px;\">").append(s.getLatestValue() != null ? s.getLatestValue() + " " + s.getUnit() : "-").append("</td>")
                    .append("</tr>");
        }

        if (report.getSensors().isEmpty()) {
            sensorRows.append("<tr><td colspan=\"8\" style=\"border:1px solid #E2CFC3; padding:8px; text-align:center; color:#A68972;\">No sensors found</td></tr>");
        }

        String aiHtml = report.getAiSummary()
                .replaceAll("\\*\\*(.+?)\\*\\*", "<b>$1</b>")
                .replace("\n\n", "</p><p style=\"margin:8px 0;\">")
                .replace("\n", "<br/>");

        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head>" +
                "<body style=\"margin:0; padding:0; background-color:#E8DED2; font-family:Arial,sans-serif;\">" +
                "<div style=\"max-width:720px; margin:auto; padding:34px 18px;\">" +
                "<div style=\"background:linear-gradient(135deg,#FFF8F4 0%,#F7E8E1 55%,#E8DED2 100%); border-radius:24px; overflow:hidden; border:1px solid #E2CFC3;\">" +

                "<div style=\"padding:24px 28px 18px; border-bottom:1px solid rgba(166,137,114,0.22);\">" +
                "<p style=\"margin:0 0 8px; display:inline-block; background-color:#F3DCD2; color:#765345; padding:7px 14px; border-radius:999px; font-size:13px;\">Sensor Report - DAR Platform</p>" +
                "<h1 style=\"margin:8px 0 4px; color:#765345; font-size:28px; font-weight:800;\">DAR</h1>" +
                "<p style=\"margin:0; color:#A68972; font-size:13px;\">Smart Home Management</p>" +
                "<p style=\"margin:8px 0 0; color:#3E302A; font-size:13px;\"><b>Home:</b> " + report.getHomeAddress() +
                " &nbsp; <b>Total Sensors:</b> " + report.getTotalSensors() +
                " &nbsp; <b>Active:</b> " + report.getActiveSensors() + "</p>" +
                "</div>" +

                "<div style=\"padding:24px 28px 0;\">" +
                "<h2 style=\"color:#765345; font-size:16px; margin:0 0 10px;\">Sensor Readings Summary</h2>" +
                "<table style=\"width:100%; border-collapse:collapse; font-size:12px;\">" +
                "<tr>" +
                "<th style=\"background-color:#765345; color:white; padding:8px; text-align:left;\">Type</th>" +
                "<th style=\"background-color:#765345; color:white; padding:8px; text-align:left;\">Location</th>" +
                "<th style=\"background-color:#765345; color:white; padding:8px; text-align:left;\">Status</th>" +
                "<th style=\"background-color:#765345; color:white; padding:8px; text-align:left;\">Readings</th>" +
                "<th style=\"background-color:#765345; color:white; padding:8px; text-align:left;\">Average</th>" +
                "<th style=\"background-color:#765345; color:white; padding:8px; text-align:left;\">Min</th>" +
                "<th style=\"background-color:#765345; color:white; padding:8px; text-align:left;\">Max</th>" +
                "<th style=\"background-color:#765345; color:white; padding:8px; text-align:left;\">Latest</th>" +
                "</tr>" +
                sensorRows +
                "</table>" +
                "</div>" +

                "<div style=\"padding:20px 28px;\">" +
                "<h2 style=\"color:#765345; font-size:16px; margin:0 0 10px;\">AI Analysis</h2>" +
                "<div style=\"background-color:rgba(255,255,255,0.72); border:1px solid rgba(166,137,114,0.24); border-radius:18px; padding:18px 20px; font-size:13px; color:#3E302A;\">" +
                "<p style=\"margin:8px 0;\">" + aiHtml + "</p>" +
                "</div>" +
                "</div>" +

                "<div style=\"background-color:#3B241C; padding:18px 28px; text-align:center;\">" +
                "<p style=\"font-size:13px; color:#E8DED2; margin:0;\">DAR Platform - Sensor Report</p>" +
                "<p style=\"font-size:12px; color:#A68972; margin:8px 0 0;\">Auto-generated report</p>" +
                "</div>" +

                "</div></div></body></html>";

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new ApiException("Failed to generate PDF: " + e.getMessage());
        }
    }

    private String translateStatus(String status) {
        if (status == null) return "-";
        return switch (status.toUpperCase()) {
            case "PAID" -> "Paid";
            case "OVERDUE" -> "Overdue";
            default -> "Pending";
        };
    }

    private String translateType(String type) {
        return switch (type.toUpperCase()) {
            case "ELECTRICITY" -> "Electricity";
            case "WATER" -> "Water";
            case "GAS" -> "Gas";
            default -> type;
        };
    }
}
