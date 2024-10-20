package com.wm.notification.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "wm_email")
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "message")
    private String message;

    @Column(name = "sender")
    private String sender;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "email")
    private String email;

    @Column(name = "wm_unique_id")
    private String wmUniqueId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Mail() {
    }

    public Mail(String subject, String message, String sender, String receiver, String email, String wmUniqueId, LocalDateTime createdAt) {
        this.subject = subject;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.email = email;
        this.wmUniqueId = wmUniqueId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWmUniqueId() {
        return wmUniqueId;
    }

    public void setWmUniqueId(String wmUniqueId) {
        this.wmUniqueId = wmUniqueId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}