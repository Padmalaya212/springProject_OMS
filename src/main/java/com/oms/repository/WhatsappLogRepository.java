package com.oms.repository;

import com.oms.entity.WhatsappLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WhatsappLogRepository extends JpaRepository<WhatsappLog, Integer> {
    List<WhatsappLog> findByOrder_OrderId(Integer orderId);
}