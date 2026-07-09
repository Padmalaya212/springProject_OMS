package com.oms.controller;


import com.oms.dto.InvoiceResponseDTO;
import com.oms.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
	@Autowired
    private  InvoiceService invoiceService;

    InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping public ResponseEntity<List<InvoiceResponseDTO>> getAll() { return ResponseEntity.ok(invoiceService.getAll()); }

    @PostMapping("/generate/{orderId}")
    public ResponseEntity<InvoiceResponseDTO> generate(@PathVariable Integer orderId) {
        return ResponseEntity.ok(invoiceService.generateInvoice(orderId));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<InvoiceResponseDTO> getByOrder(@PathVariable Integer orderId) {
        return ResponseEntity.ok(invoiceService.getByOrderId(orderId));
    }
}