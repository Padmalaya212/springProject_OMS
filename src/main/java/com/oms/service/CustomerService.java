package com.oms.service;

import com.oms.dto.CustomerDTO;
import com.oms.entity.Customer;
import com.oms.exception.ResourceNotFoundException;
import com.oms.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

	@Autowired
    private CustomerRepository customerRepository;

    CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerDTO> getAll() {
        return customerRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public CustomerDTO getById(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
        return mapToDTO(customer);
    }

    public CustomerDTO update(Integer id, CustomerDTO dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));

        customer.setFullName(dto.getFullName());
        customer.setAddress(dto.getAddress());
        customer.setCity(dto.getCity());
        customer.setState(dto.getState());
        customer.setPincode(dto.getPincode());

        if (customer.getUser() != null) {
            customer.getUser().setEmail(dto.getEmail());
            customer.getUser().setPhone(dto.getPhone());
           
        }
        
        Customer saved = customerRepository.save(customer);
        return mapToDTO(saved);
    }

    public long getTotalCount() {
        return customerRepository.count();
    }

    private CustomerDTO mapToDTO(Customer c) {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(c.getCustomerId());
        dto.setFullName(c.getFullName());
        dto.setAddress(c.getAddress());
        dto.setCity(c.getCity());
        dto.setState(c.getState());
        dto.setPincode(c.getPincode());
        dto.setCreatedAt(c.getCreatedAt());
        if (c.getUser() != null) {
            dto.setUsername(c.getUser().getUsername());
            dto.setEmail(c.getUser().getEmail());
            dto.setPhone(c.getUser().getPhone());
        }
        return dto;
    }
}