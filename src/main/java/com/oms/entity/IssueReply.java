package com.oms.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "issue_reply")
@Data
public class IssueReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer replyId;

    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false)
    @JsonIgnore
    private CustomerIssue issue;

    @ManyToOne
    @JoinColumn(name = "replied_by", nullable = false)
    private User repliedBy;

    private String replyMessage;
    public Integer getReplyId() {
		return replyId;
	}
	public void setReplyId(Integer replyId) {
		this.replyId = replyId;
	}
	public CustomerIssue getIssue() {
		return issue;
	}
	public void setIssue(CustomerIssue issue) {
		this.issue = issue;
	}
	public User getRepliedBy() {
		return repliedBy;
	}
	public void setRepliedBy(User repliedBy) {
		this.repliedBy = repliedBy;
	}
	public String getReplyMessage() {
		return replyMessage;
	}
	public void setReplyMessage(String replyMessage) {
		this.replyMessage = replyMessage;
	}
	public LocalDateTime getRepliedAt() {
		return repliedAt;
	}
	public void setRepliedAt(LocalDateTime repliedAt) {
		this.repliedAt = repliedAt;
	}
	private LocalDateTime repliedAt = LocalDateTime.now();
}