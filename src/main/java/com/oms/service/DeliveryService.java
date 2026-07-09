package com.oms.service;

import com.oms.dto.DeliveryResponseDTO;
import com.oms.entity.Delivery;
import com.oms.entity.Orders;
import com.oms.exception.ResourceNotFoundException;
import com.oms.repository.DeliveryRepository;
import com.oms.repository.OrdersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService {
	@Autowired
    private  DeliveryRepository deliveryRepository;
	@Autowired private   OrdersRepository ordersRepository;

    DeliveryService(DeliveryRepository deliveryRepository, OrdersRepository ordersRepository) {
        this.deliveryRepository = deliveryRepository;
        this.ordersRepository = ordersRepository;
    }

    public List<DeliveryResponseDTO> getAll() {
        return deliveryRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public DeliveryResponseDTO create(Integer orderId, String address) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setDeliveryAddress(address);
        Delivery saved = deliveryRepository.save(delivery);
        return mapToDTO(saved);
    }

    public DeliveryResponseDTO updateStatus(Integer deliveryId, String status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found: " + deliveryId));
        delivery.setDeliveryStatus(Delivery.DeliveryStatus.valueOf(status));
        Delivery saved = deliveryRepository.save(delivery);

        // sync the linked Order's status based on delivery status
        Orders order = saved.getOrder();
        if ("DISPATCHED".equals(status)) {
            order.setOrderStatus(Orders.OrderStatus.OUT_FOR_DELIVERY);
        } else if ("DELIVERED".equals(status)) {
            order.setOrderStatus(Orders.OrderStatus.DELIVERED);
        } else if ("FAILED".equals(status)) {
            order.setOrderStatus(Orders.OrderStatus.CANCELLED);
        }
        ordersRepository.save(order);

        return mapToDTO(saved);
    }

    private DeliveryResponseDTO mapToDTO(Delivery d) {
        DeliveryResponseDTO dto = new DeliveryResponseDTO();
        dto.setDeliveryId(d.getDeliveryId());
        dto.setOrderNumber(d.getOrder().getOrderNumber());
        dto.setDeliveryAddress(d.getDeliveryAddress());
        dto.setDeliveryStatus(d.getDeliveryStatus().name());
        dto.setExpectedDate(d.getExpectedDate());
        dto.setDeliveredDate(d.getDeliveredDate());
        return dto;
    }
}