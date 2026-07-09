package com.oms.controller;

import com.oms.dto.CustomerIssueDTO;
import com.oms.dto.IssueReplyDTO;
import com.oms.service.CustomerIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class CustomerIssueController {
	@Autowired
    private CustomerIssueService issueService;

    CustomerIssueController(CustomerIssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping public ResponseEntity<List<CustomerIssueDTO>> getAll() { return ResponseEntity.ok(issueService.getAll()); }

    @PostMapping
    public ResponseEntity<CustomerIssueDTO> create(@RequestBody CustomerIssueDTO dto) {
        return ResponseEntity.ok(issueService.create(dto));
    }

    @PostMapping("/{issueId}/reply")
    public ResponseEntity<IssueReplyDTO> reply(@PathVariable Integer issueId,
                                                @RequestParam Integer repliedByUserId,
                                                @RequestParam String message) {
        return ResponseEntity.ok(issueService.addReply(issueId, repliedByUserId, message));
    }

    @PutMapping("/{issueId}/status")
    public ResponseEntity<CustomerIssueDTO> updateStatus(@PathVariable Integer issueId, @RequestParam String status) {
        return ResponseEntity.ok(issueService.updateStatus(issueId, status));
    }
}