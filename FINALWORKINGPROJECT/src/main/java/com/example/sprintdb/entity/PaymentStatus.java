package com.example.sprintdb.entity;

import javax.persistence.*;

@Entity
@Table(name = "payment_status")
public class PaymentStatus {

    public enum StatusType {
        PENDING,
        SUCCESS,
        FAILED,
        REFUNDED,
        CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long statusId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 30)
    private StatusType status;

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }
}