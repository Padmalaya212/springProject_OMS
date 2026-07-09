package com.oms.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customer_issue")
@Data
public class CustomerIssue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer issueId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    private String subject;
    private String description;

    public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Orders getOrder() {
		return order;
	}

	public void setOrder(Orders order) {
		this.order = order;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public IssueStatus getStatus() {
		return status;
	}

	public void setStatus(IssueStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<IssueReply> getReplies() {
		return replies;
	}

	public void setReplies(List<IssueReply> replies) {
		this.replies = replies;
	}

	@Enumerated(EnumType.STRING)
    private IssueStatus status = IssueStatus.OPEN;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<IssueReply> replies;

    public enum IssueStatus { OPEN, IN_PROGRESS, RESOLVED, CLOSED }
}