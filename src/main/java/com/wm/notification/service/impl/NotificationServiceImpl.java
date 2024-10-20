package com.wm.notification.service.impl;

import com.wm.notification.dto.NotificationPayload;
import com.wm.notification.enums.SourceApp;
import com.wm.notification.enums.UserType;
import com.wm.notification.exception.*;
import com.wm.notification.model.Notification;
import com.wm.notification.response.BaseResponse;
import com.wm.notification.respository.NotificationRepository;
import com.wm.notification.service.NotificationService;
import com.wm.notification.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.wm.notification.constants.ApiConstants.REDIS_HASH_KEY;
import static com.wm.notification.constants.GeneralConstants.OPERATION_SUCCESSFUL;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger((NotificationServiceImpl.class));
    private final NotificationRepository notificationRepo;

    private final UserService userService;

    private final RedisTemplate redisTemplate;


    public NotificationServiceImpl(NotificationRepository notificationRepo,
                                   UserService userService, RedisTemplate redisTemplate) {
        this.notificationRepo = notificationRepo;
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void readMessage(String wmUniqueId, Long id) {

        if (!userService.existsByWmUniqueId(wmUniqueId)) {
            throw new UserNotFoundException("User does not exist");
        }

        Notification notification = notificationRepo.findById(id).orElseThrow(() -> new NotificationNotFoundException("Notification not found"));

        if (!notification.isRead()) {
            // Mark notification as read if it's not already read
            notification.setRead(true);
            notificationRepo.save(notification);
        }
    }

    @Override
    public BaseResponse getAllNotifications() {

        BaseResponse response = new BaseResponse();

        List<Notification> notifications = notificationRepo.findAll();
        List<HashMap<String, Object>> notificationDataList = new ArrayList<>();

        for (Notification notification : notifications) {
            HashMap<String, Object> notificationData = buildNotificationData(notification);
            notificationDataList.add(notificationData);
        }

        if (!notificationDataList.isEmpty()) {
            response.setData(notificationDataList);
            response.setStatusCode(HttpServletResponse.SC_OK);
            response.setDescription(OPERATION_SUCCESSFUL);
        } else {
            response.setData(new ArrayList<>());
            response.setDescription(OPERATION_SUCCESSFUL);
            response.setStatusCode(HttpServletResponse.SC_OK);
        }
        return response;
    }

    @Override
    public BaseResponse getUserNotifications(String wmUniqueId, UserType userType, SourceApp sourceApp) {

        BaseResponse response = new BaseResponse();

        if (!userService.existsByWmUniqueId(wmUniqueId)) {
            throw new UserNotFoundException("User does not exist");
        }

        List<Notification> notifications = new ArrayList<>();

        switch (userType) {
            case BUYER:
            case SELLER:
                if (sourceApp == SourceApp.SERVICE_MARKETPLACE_APP) {
                    notifications = getUserCachedNotifications(wmUniqueId);
                    if (notifications == null || notifications.isEmpty()) {
                        notifications = notificationRepo.findAllByReceiverAndUserTypeAndSourceAppOrderByCreatedAtDesc(wmUniqueId, userType, sourceApp);
                    }
                    break;
                }
            case PORTAL_USER:
                notifications = getUserCachedNotifications(wmUniqueId);
                if (notifications == null || notifications.isEmpty()) {
                    notifications = notificationRepo.findAllByReceiverAndSourceAppOrderByCreatedAtDesc(wmUniqueId, sourceApp);
                }
                break;
            default:
                response.setDescription("Invalid user type");
                response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
                return response;
        }

        if (!notifications.isEmpty()) {
            List<HashMap<String, Object>> notificationDataList = new ArrayList<>();
            for (Notification notification : notifications) {
                HashMap<String, Object> notificationData = buildNotificationData(notification);
                notificationDataList.add(notificationData);
            }
            response.setData(notificationDataList);
        } else {
            response.setData(new ArrayList<>());
            response.setDescription("No notifications found");
        }

        response.setDescription("Operation successful");
        response.setStatusCode(HttpServletResponse.SC_OK);
        return response;
    }

    private List<Notification> getUserCachedNotifications(String receiverId) {
        List<NotificationPayload> cachedNotifications = new ArrayList<>();
        List<Notification> notifications = new ArrayList<>();

        try {

            // This is to create the redis key for retrieving the cached notifications
            String redisKey = REDIS_HASH_KEY + receiverId;

            // This is to retrieve a list of cached notifications
            cachedNotifications = redisTemplate.opsForList().range(redisKey, 0, -1);

            if (cachedNotifications == null || cachedNotifications.isEmpty()) {
                LOGGER.error("No cached notifications found for this redis_key: {}", redisKey);
            } else  {
                LOGGER.info("Successfully retrieved cached notifications for redis_key: {}", redisKey);

                // This is to convert NotificationPayload to Notifications and add it to the list
                for (NotificationPayload notificationPayload: cachedNotifications) {
                    notifications.add(convertToNotification(notificationPayload));
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Failed to retrieve cached notifications from redis, falling back to database: {}", ex.getMessage());
        }

        return notifications;
    }

    private Notification convertToNotification(NotificationPayload notificationPayload) {
        Notification notification = new Notification();

        notification.setSender(notificationPayload.getSender());
        notification.setSenderUniqueId(notificationPayload.getSenderUniqueId());
        notification.setReceiver(notificationPayload.getReceiverId());
        notification.setMessage(notificationPayload.getMessage());
        notification.setUserType(notificationPayload.getUserType());
        notification.setEvents(notificationPayload.getEvents());
        notification.setSourceApp(notificationPayload.getSourceApp());
        notification.setId(notificationPayload.getId());
        notification.setCreatedAt(notificationPayload.getCreatedAt());

        return notification;
    }

    private HashMap<String, Object> buildNotificationData(Notification notification){

        HashMap<String, Object> result = new HashMap<>();

        result.put("id", notification.getId());
        result.put("message", notification.getMessage());
        result.put("senderName", notification.getSender());
        result.put("receiver", notification.getReceiver());
        result.put("createdDate", notification.getCreatedAt());
        result.put("read", notification.isRead());
        result.put("eventType", notification.getEvents());

        String imageUrl = userService.getUserProfilePictureUrl(notification.getSenderUniqueId());
        result.put("senderImage", imageUrl);

        return result;
    }
}