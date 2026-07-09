package com.oms.controller;


import com.oms.dto.DeliveryResponseDTO;
import com.oms.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {
	@Autowired
    private  DeliveryService deliveryService;

    DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping public ResponseEntity<List<DeliveryResponseDTO>> getAll() { return ResponseEntity.ok(deliveryService.getAll()); }

    @PostMapping("/order/{orderId}")
    public ResponseEntity<DeliveryResponseDTO> create(@PathVariable Integer orderId, @RequestParam String address) {
        return ResponseEntity.ok(deliveryService.create(orderId, address));
    }

    @PutMapping("/{deliveryId}/status")
    public ResponseEntity<DeliveryResponseDTO> updateStatus(@PathVariable Integer deliveryId, @RequestParam String status) {
        return ResponseEntity.ok(deliveryService.updateStatus(deliveryId, status));
    }
}