package com.oms.service;


import com.oms.entity.Product;
import com.oms.entity.Stock;
import com.oms.entity.StockHistory;
import com.oms.exception.ResourceNotFoundException;
import com.oms.repository.ProductRepository;
import com.oms.repository.StockHistoryRepository;
import com.oms.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StockService {
   
	@Autowired private  StockRepository stockRepository;
	@Autowired private ProductRepository productRepository;
    @Autowired private  StockHistoryRepository stockHistoryRepository;

    StockService(StockRepository stockRepository, ProductRepository productRepository, StockHistoryRepository stockHistoryRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.stockHistoryRepository = stockHistoryRepository;
    }

    public List<Stock> getAll() { return stockRepository.findAll(); }

    public Stock getByProductId(Integer productId) {
        return stockRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for product: " + productId));
    }

    public Stock adjustStock(Integer productId, Integer quantityChange, String changeType, String reason) {
        Stock stock = getByProductId(productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));

        int newQty = stock.getQuantity();
        if (changeType.equals("ADD")) newQty += quantityChange;
        else if (changeType.equals("REMOVE")) newQty -= quantityChange;
        else newQty = quantityChange; // ADJUST sets absolute value

        if (newQty < 0) throw new RuntimeException("Stock cannot go negative");

        stock.setQuantity(newQty);
        Stock saved = stockRepository.save(stock);

        StockHistory history = new StockHistory();
        history.setProduct(product);
        history.setChangeType(StockHistory.ChangeType.valueOf(changeType));
        history.setQuantityChanged(quantityChange);
        history.setReason(reason);
        stockHistoryRepository.save(history);

        return saved;
    }

    public List<StockHistory> getHistory(Integer productId) {
        return stockHistoryRepository.findByProduct_ProductId(productId);
    }
}