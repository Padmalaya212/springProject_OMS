package com.oms.dto;


import java.time.LocalDate;


public class DeliveryResponseDTO {
    private Integer deliveryId;
    private String orderNumber;
    private String deliveryAddress;
    private String deliveryStatus;
    private LocalDate expectedDate;
    private LocalDate deliveredDate;
	public Integer getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public String getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
	public LocalDate getExpectedDate() {
		return expectedDate;
	}
	public void setExpectedDate(LocalDate expectedDate) {
		this.expectedDate = expectedDate;
	}
	public LocalDate getDeliveredDate() {
		return deliveredDate;
	}
	public void setDeliveredDate(LocalDate deliveredDate) {
		this.deliveredDate = deliveredDate;
	}
}