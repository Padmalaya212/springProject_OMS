package com.oms.service;


import com.oms.dto.OrderRequestDTO;
import com.oms.dto.OrderResponseDTO;
import com.oms.entity.*;
import com.oms.exception.ResourceNotFoundException;
import com.oms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired private  OrdersRepository ordersRepository;
    @Autowired private  OrderItemRepository orderItemRepository;
    @Autowired private  CustomerRepository customerRepository;
    @Autowired  private  ProductRepository productRepository;
    @Autowired private  StockService stockService;
    @Autowired private CustomerIssueRepository customerIssueRepository;
    @Autowired private InvoiceRepository invoiceRepository;
    @Autowired private DeliveryRepository deliveryRepository;

    
    OrderService(OrdersRepository ordersRepository, OrderItemRepository orderItemRepository, CustomerRepository customerRepository, ProductRepository productRepository, StockService stockService, CustomerIssueRepository customerIssueRepository, InvoiceRepository invoiceRepository, DeliveryRepository deliveryRepository) {
        this.ordersRepository = ordersRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.stockService = stockService;
        this.customerIssueRepository = customerIssueRepository;
        this.invoiceRepository = invoiceRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public List<OrderResponseDTO> getAll() {
        return ordersRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public OrderResponseDTO getById(Integer id) {
        return mapToDTO(findEntity(id));
    }

    public Orders findEntity(Integer id) {
        return ordersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
    }

    public OrderResponseDTO createOrder(OrderRequestDTO req) {
        Customer customer = customerRepository.findById(req.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + req.getCustomerId()));

        Orders order = new Orders();
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        order.setCustomer(customer);
        order.setOrderStatus(Orders.OrderStatus.PENDING);

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();

        for (OrderRequestDTO.OrderItemRequestDTO itemReq : req.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemReq.getProductId()));

            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            BigDecimal gst = subtotal.multiply(product.getGstPercentage()).divide(BigDecimal.valueOf(100));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(unitPrice);
            item.setGstAmount(gst);
            item.setSubtotal(subtotal.add(gst));
            items.add(item);

            total = total.add(subtotal).add(gst);

            // reduce stock automatically
            stockService.adjustStock(product.getProductId(), itemReq.getQuantity(), "REMOVE", "Order placed");
        }

        order.setTotalAmount(total);
        order.setItems(items);
        Orders saved = ordersRepository.save(order);
        orderItemRepository.saveAll(items);

        return mapToDTO(saved);
    }

    public OrderResponseDTO updateStatus(Integer id, String status) {
        Orders order = findEntity(id);
        order.setOrderStatus(Orders.OrderStatus.valueOf(status));
        return mapToDTO(ordersRepository.save(order));
    }

    
    public void delete(Integer id) {
        Orders order = findEntity(id);

        boolean hasIssues = !customerIssueRepository.findAll().stream()
                .filter(i -> i.getOrder() != null && i.getOrder().getOrderId().equals(id))
                .toList().isEmpty();
        boolean hasInvoice = invoiceRepository.findByOrder_OrderId(id).isPresent();
        boolean hasDelivery = deliveryRepository.findByOrder_OrderId(id).isPresent();

        if (hasIssues || hasInvoice || hasDelivery) {
            throw new RuntimeException(
                "Cannot delete this order — it has linked records (invoice, delivery, or support issue). " +
                "Cancel the order instead by updating its status."
            );
        }

        ordersRepository.delete(order);
    }
    
    private OrderResponseDTO mapToDTO(Orders order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getOrderId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setCustomerId(order.getCustomer().getCustomerId());
        dto.setCustomerName(order.getCustomer().getFullName());
        dto.setOrderStatus(order.getOrderStatus().name());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderDate(order.getOrderDate());

        List<OrderItem> items = orderItemRepository.findByOrder_OrderId(order.getOrderId());
        List<OrderResponseDTO.OrderItemResponseDTO> itemDTOs = items.stream().map(item -> {
            OrderResponseDTO.OrderItemResponseDTO i = new OrderResponseDTO.OrderItemResponseDTO();
            i.setProductId(item.getProduct().getProductId());
            i.setProductName(item.getProduct().getProductName());
            i.setQuantity(item.getQuantity());
            i.setUnitPrice(item.getUnitPrice());
            i.setSubtotal(item.getSubtotal());
            return i;
        }).collect(Collectors.toList());
        dto.setItems(itemDTOs);

        return dto;
    }
}