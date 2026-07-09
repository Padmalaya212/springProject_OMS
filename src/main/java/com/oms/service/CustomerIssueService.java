package com.oms.service;


import com.oms.dto.CustomerIssueDTO;
import com.oms.dto.IssueReplyDTO;
import com.oms.entity.*;
import com.oms.exception.ResourceNotFoundException;
import com.oms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerIssueService {
	@Autowired
    private  CustomerIssueRepository issueRepository;
	@Autowired private  CustomerRepository customerRepository;
	@Autowired private  OrdersRepository ordersRepository;
	@Autowired private  IssueReplyRepository issueReplyRepository;
	@Autowired private  UserRepository userRepository;

    CustomerIssueService(CustomerIssueRepository issueRepository, CustomerRepository customerRepository, OrdersRepository ordersRepository, IssueReplyRepository issueReplyRepository, UserRepository userRepository) {
        this.issueRepository = issueRepository;
        this.customerRepository = customerRepository;
        this.ordersRepository = ordersRepository;
        this.issueReplyRepository = issueReplyRepository;
        this.userRepository = userRepository;
    }

    public List<CustomerIssueDTO> getAll() {
        return issueRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public CustomerIssueDTO create(CustomerIssueDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + dto.getCustomerId()));

        CustomerIssue issue = new CustomerIssue();
        issue.setCustomer(customer);
        issue.setSubject(dto.getSubject());
        issue.setDescription(dto.getDescription());

        if (dto.getOrderId() != null) {
            Orders order = ordersRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + dto.getOrderId()));
            issue.setOrder(order);
        }

        return mapToDTO(issueRepository.save(issue));
    }

    public IssueReplyDTO addReply(Integer issueId, Integer repliedByUserId, String message) {
        CustomerIssue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found: " + issueId));
        User user = userRepository.findById(repliedByUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + repliedByUserId));

        IssueReply reply = new IssueReply();
        reply.setIssue(issue);
        reply.setRepliedBy(user);
        reply.setReplyMessage(message);
        IssueReply saved = issueReplyRepository.save(reply);

        issue.setStatus(CustomerIssue.IssueStatus.IN_PROGRESS);
        issueRepository.save(issue);

        IssueReplyDTO dto = new IssueReplyDTO();
        dto.setReplyId(saved.getReplyId());
        dto.setIssueId(issueId);
        dto.setRepliedBy(repliedByUserId);
        dto.setRepliedByName(user.getUsername());
        dto.setReplyMessage(message);
        return dto;
    }

    public CustomerIssueDTO updateStatus(Integer issueId, String status) {
        CustomerIssue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found: " + issueId));
        issue.setStatus(CustomerIssue.IssueStatus.valueOf(status));
        return mapToDTO(issueRepository.save(issue));
    }

    private CustomerIssueDTO mapToDTO(CustomerIssue issue) {
        CustomerIssueDTO dto = new CustomerIssueDTO();
        dto.setIssueId(issue.getIssueId());
        dto.setCustomerId(issue.getCustomer().getCustomerId());
        dto.setCustomerName(issue.getCustomer().getFullName());
        dto.setOrderId(issue.getOrder() != null ? issue.getOrder().getOrderId() : null);
        dto.setSubject(issue.getSubject());
        dto.setDescription(issue.getDescription());
        dto.setStatus(issue.getStatus().name());
        return dto;
    }
}