package com.oms.service;

import com.oms.dto.InvoiceResponseDTO;
import com.oms.entity.Invoice;
import com.oms.entity.Orders;
import com.oms.exception.ResourceNotFoundException;
import com.oms.repository.InvoiceRepository;
import com.oms.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
	@Autowired
	private   InvoiceRepository invoiceRepository;
	@Autowired private  OrdersRepository ordersRepository;


    InvoiceService(InvoiceRepository invoiceRepository, OrdersRepository ordersRepository) {
        this.invoiceRepository = invoiceRepository;
        this.ordersRepository = ordersRepository;
    }


    public List<InvoiceResponseDTO> getAll() {
        return invoiceRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public InvoiceResponseDTO generateInvoice(Integer orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        Invoice invoice = new Invoice();
        invoice.setOrder(order);
        invoice.setInvoiceNumber("INV-" + System.currentTimeMillis());
        invoice.setTotalAmount(order.getTotalAmount());
        invoice.setGstAmount(order.getTotalAmount().multiply(new java.math.BigDecimal("0.05"))); // example 5% shown separately
        Invoice saved = invoiceRepository.save(invoice);
        return mapToDTO(saved);
    }

    public InvoiceResponseDTO getByOrderId(Integer orderId) {
        Invoice invoice = invoiceRepository.findByOrder_OrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found for order: " + orderId));
        return mapToDTO(invoice);
    }

    private InvoiceResponseDTO mapToDTO(Invoice invoice) {
        InvoiceResponseDTO dto = new InvoiceResponseDTO();
        dto.setInvoiceId(invoice.getInvoiceId());
        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        dto.setOrderNumber(invoice.getOrder().getOrderNumber());
        dto.setCustomerName(invoice.getOrder().getCustomer().getFullName());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setGstAmount(invoice.getGstAmount());
        dto.setGeneratedAt(invoice.getGeneratedAt());
        return dto;
    }
}