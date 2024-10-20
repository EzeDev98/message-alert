package com.wm.notification.dto;

import com.wm.notification.enums.Events;
import com.wm.notification.enums.SourceApp;
import com.wm.notification.enums.UserType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@RedisHash("NotificationPayload")
public class NotificationPayload {

    private Long id;

    private String sender;

    private String senderUniqueId;

    private String message;

    private String receiverId;

    private Events events;

    private UserType userType;

    private SourceApp sourceApp;

    private LocalDateTime createdAt;

    public NotificationPayload() {
    }

    public NotificationPayload(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {
        this.sender = sender;
        this.senderUniqueId = senderUniqueId;
        this.message = message;
        this.receiverId = receiverId;
        this.events = events;
        this.userType = userType;
        this.sourceApp = sourceApp;
    }
}