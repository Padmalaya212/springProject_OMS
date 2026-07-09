package com.oms.repository;

import com.oms.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    Optional<Orders> findByOrderNumber(String orderNumber);
    List<Orders> findByCustomer_CustomerId(Integer customerId);
    List<Orders> findByOrderStatus(Orders.OrderStatus status);
}