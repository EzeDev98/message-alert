package com.wm.notification.model;

import com.wm.notification.enums.Events;
import com.wm.notification.enums.SourceApp;
import com.wm.notification.enums.UserType;
import jakarta.persistence.*;
import org.apache.catalina.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "wm_notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;

    private String senderUniqueId;

    @Column(name = "message")
    private String message;

    @Column(name = "receiver_id")
    private String receiver;

    private boolean read;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "userType")
    private UserType userType;

    @Enumerated(EnumType.STRING)
    @Column(name = "sourceApp")
    private SourceApp sourceApp;

    @Enumerated(EnumType.STRING)
    @Column(name = "events")
    private Events events;

    public Notification() {
    }

    public Notification(String sender, String senderUniqueId, String message, String receiver, boolean read, LocalDateTime createdAt,
                        UserType userType, SourceApp sourceApp, Events events) {
        this.sender = sender;
        this.senderUniqueId = senderUniqueId;
        this.message = message;
        this.receiver = receiver;
        this.read = read;
        this.createdAt = createdAt;
        this.userType = userType;
        this.sourceApp = sourceApp;
        this.events = events;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderUniqueId() {
        return senderUniqueId;
    }

    public void setSenderUniqueId(String senderUniqueId) {
        this.senderUniqueId = senderUniqueId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public SourceApp getSourceApp() {
        return sourceApp;
    }

    public void setSourceApp(SourceApp sourceApp) {
        this.sourceApp = sourceApp;
    }

    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }
}