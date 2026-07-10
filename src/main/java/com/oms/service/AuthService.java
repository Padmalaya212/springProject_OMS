package com.oms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.oms.entity.*;
import com.oms.exception.ResourceNotFoundException;
import com.oms.dto.*;
import com.oms.repository.*;


@Service
public class AuthService {
	@Autowired
    private  UserRepository userRepository;
	@Autowired private  RoleRepository roleRepository;
	@Autowired private  CustomerRepository customerRepository;

    AuthService(UserRepository userRepository, RoleRepository roleRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
    }

    public AuthResponseDTO register(RegisterRequestDTO req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        Role customerRole = roleRepository.findByRoleName("CUSTOMER");
        
        if (customerRole == null) {
            customerRole = new Role();
            customerRole.setRoleId(2); // Maps to standard user ID assignment constraint
            customerRole.setRoleName("CUSTOMER");
            try {
                customerRole = roleRepository.save(customerRole);
            } catch (StringIndexOutOfBoundsException | NullPointerException e) {
                
            }
        }
        
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setRole(customerRole);
        User savedUser = userRepository.save(user);
        Customer customer = new Customer();
        customer.setUser(savedUser);
        customer.setFullName(req.getFullName());
        customer.setAddress(req.getAddress());
        customer.setCity(req.getCity());
        customer.setState(req.getState());
        customer.setPincode(req.getPincode());
        customerRepository.save(customer);
        AuthResponseDTO res = new AuthResponseDTO();
        res.setUserId(savedUser.getUserId());
        res.setUsername(savedUser.getUsername());
        res.setRole(customerRole.getRoleName());
        res.setMessage("Registration successful");
        return res;
    }
    
    public AuthResponseDTO login(LoginRequestDTO req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid username or password"));

        if (!user.getPassword().equals(req.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        AuthResponseDTO res = new AuthResponseDTO();
        res.setUserId(user.getUserId());
        res.setUsername(user.getUsername());
        res.setRole(user.getRole().getRoleName());
        res.setMessage("Login successful");
        return res;
    }
	
}