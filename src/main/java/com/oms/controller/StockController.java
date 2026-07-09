package com.oms.controller;

import com.oms.entity.Stock;
import com.oms.entity.StockHistory;
import com.oms.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {
	@Autowired
    private  StockService stockService;

    StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping public ResponseEntity<List<Stock>> getAll() { return ResponseEntity.ok(stockService.getAll()); }

    @GetMapping("/{productId}")
    public ResponseEntity<Stock> getByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(stockService.getByProductId(productId));
    }

    @PutMapping("/{productId}/adjust")
    public ResponseEntity<Stock> adjust(@PathVariable Integer productId,
                                         @RequestParam Integer quantity,
                                         @RequestParam String changeType,
                                         @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(stockService.adjustStock(productId, quantity, changeType, reason));
    }

    @GetMapping("/{productId}/history")
    public ResponseEntity<List<StockHistory>> history(@PathVariable Integer productId) {
        return ResponseEntity.ok(stockService.getHistory(productId));
    }
}