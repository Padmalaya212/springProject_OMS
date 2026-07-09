package com.oms.entity;



import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "whatsapp_logs")
@Data
public class WhatsappLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    private String message;

    public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Orders getOrder() {
		return order;
	}

	public void setOrder(Orders order) {
		this.order = order;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public SentStatus getSentStatus() {
		return sentStatus;
	}

	public void setSentStatus(SentStatus sentStatus) {
		this.sentStatus = sentStatus;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}

	@Enumerated(EnumType.STRING)
    private SentStatus sentStatus = SentStatus.SENT;

    private LocalDateTime sentAt = LocalDateTime.now();

    public enum SentStatus { SENT, FAILED }
}