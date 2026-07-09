package com.oms.service;

import com.oms.entity.Orders;
import com.oms.repository.OrdersRepository;
import com.oms.repository.CustomerRepository;
import com.oms.repository.ProductRepository;
import com.oms.repository.CustomerIssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class ReportService {
	@Autowired
    private  OrdersRepository ordersRepository;
	@Autowired private  CustomerRepository customerRepository;
	@Autowired private  ProductRepository productRepository;
	@Autowired private  CustomerIssueRepository customerIssueRepository;

    ReportService(OrdersRepository ordersRepository, ProductRepository productRepository, CustomerRepository customerRepository, CustomerIssueRepository customerIssueRepository) {
        this.ordersRepository = ordersRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.customerIssueRepository = customerIssueRepository;
    }

    public Map<String, Object> getDashboardSummary() {
        List<Orders> allOrders = ordersRepository.findAll();
        Map<String, Object> summary = new HashMap<>();

        BigDecimal totalRevenue = allOrders.stream()
                .map(Orders::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        summary.put("totalOrders", allOrders.size());
        summary.put("totalRevenue", totalRevenue);
        summary.put("pendingOrders", allOrders.stream().filter(o -> o.getOrderStatus() == Orders.OrderStatus.PENDING).count());
        summary.put("deliveredOrders", allOrders.stream().filter(o -> o.getOrderStatus() == Orders.OrderStatus.DELIVERED).count());
        summary.put("totalCustomers", customerRepository.count());
        summary.put("totalProducts", productRepository.count());
        summary.put("totalIssues", customerIssueRepository.count());

        return summary;
    }

    public List<Orders> getOrdersByDateRange(LocalDate start, LocalDate end) {
        return ordersRepository.findAll().stream()
                .filter(o -> {
                    LocalDate orderDate = o.getOrderDate().toLocalDate();
                    return !orderDate.isBefore(start) && !orderDate.isAfter(end);
                })
                .collect(Collectors.toList());
    }
}