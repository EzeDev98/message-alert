package com.wm.notification.service.impl.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wm.notification.dto.NotificationPayload;
import com.wm.notification.enums.Events;
import com.wm.notification.enums.SourceApp;
import com.wm.notification.enums.UserType;
import com.wm.notification.exception.NotificationNotSavedException;
import com.wm.notification.model.Notification;
import com.wm.notification.response.BaseResponse;
import com.wm.notification.respository.NotificationRepository;
import com.wm.notification.service.impl.NotificationServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.wm.notification.constants.ApiConstants.REDIS_HASH_KEY;

@Component
public class EventNotificationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;

    private final NotificationRepository notificationRepo;

    public EventNotificationListener(NotificationRepository notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    private void handleNotificationMessage(String message) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {

            TypeReference<NotificationPayload> reference = new TypeReference<NotificationPayload>() {};
            NotificationPayload payload = objectMapper.readValue(message, reference);

            processNotificationPayload(payload);

        } catch (JsonProcessingException e) {
            LOGGER.error("Error processing notification message: {}", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error handling notification message: {}", e.getMessage());
        }
    }

    private void processNotificationPayload(NotificationPayload payload) {

        // Process the payload based on the event type
        switch (payload.getEvents()) {
            case JOB_APPLICATION_REQUEST:
                handleJobApplicationRequest(payload.getSender(), payload.getSenderUniqueId(), payload.getMessage(), payload.getReceiverId(), payload.getEvents(), payload.getUserType(), payload.getSourceApp());
                break;
            case CUSTOM_SERVICE_REQUEST:
                handleCustomServiceRequest(payload.getSender(), payload.getSenderUniqueId(), payload.getMessage(), payload.getReceiverId(), payload.getEvents(), payload.getUserType(), payload.getSourceApp());
                break;
            case CUSTOM_SERVICE_REQUEST_STATUS:
                handleCustomServiceRequestStatus(payload.getSender(), payload.getSenderUniqueId(), payload.getMessage(), payload.getReceiverId(), payload.getEvents(), payload.getUserType(), payload.getSourceApp());
                break;
            case JOB_INVITATION_REQUEST:
                handleInvitationRequest(payload.getSender(), payload.getSenderUniqueId(), payload.getMessage(), payload.getReceiverId(), payload.getEvents(), payload.getUserType(), payload.getSourceApp());
                break;
            case JOB_INVITATION_REQUEST_STATUS:
                handleJobInvitationRequestStatus(payload.getSender(), payload.getSenderUniqueId(), payload.getMessage(), payload.getReceiverId(), payload.getEvents(), payload.getUserType(), payload.getSourceApp());
                break;
            case WARRANT_TRANSFER:
                handleWarrantTransfer(payload.getSender(), payload.getSenderUniqueId(), payload.getMessage(), payload.getReceiverId(), payload.getEvents(), payload.getUserType(), payload.getSourceApp());
                break;
            case OFFER_REQUEST:
                handleOfferRequest(payload.getSender(), payload.getSenderUniqueId(), payload.getMessage(), payload.getReceiverId(), payload.getEvents(), payload.getUserType(), payload.getSourceApp());
                break;
            case OFFER_REQUEST_STATUS:
                handleOfferRequestStatus(payload.getSender(), payload.getSenderUniqueId(), payload.getMessage(), payload.getReceiverId(), payload.getEvents(), payload.getUserType(), payload.getSourceApp());
                break;
            case SERVICE_ORDER_PAYMENT:
                handleServiceOrderPayment(payload.getSender(), payload.getSenderUniqueId(), payload.getMessage(), payload.getReceiverId(), payload.getEvents(), payload.getUserType(), payload.getSourceApp());
                break;
            case PAYMENT_RELEASED:
                handlePaymentReleased(payload.getSender(), payload.getSenderUniqueId(), payload.getMessage(), payload.getReceiverId(), payload.getEvents(), payload.getUserType(), payload.getSourceApp());
                break;
            case GUARANTOR_REQUEST:
                handleGuarantorRequest(payload.getSender(), payload.getSenderUniqueId(), payload.getMessage(), payload.getReceiverId(), payload.getEvents(), payload.getUserType(), payload.getSourceApp());
                break;
            case GUARANTOR_REQUEST_STATUS:
                handleGuarantorRequestStatus(payload.getSender(), payload.getSenderUniqueId(), payload.getMessage(), payload.getReceiverId(), payload.getEvents(), payload.getUserType(), payload.getSourceApp());
                break;
            case SERVICE_ORDER_APPROVAL_REQUEST:
                handleServiceOrderApprovalRequest(payload.getSender(), payload.getSenderUniqueId(), payload.getMessage(), payload.getReceiverId(), payload.getEvents(), payload.getUserType(), payload.getSourceApp());
                break;
            case SERVICE_ORDER_APPROVAL:
                handleServiceOrderApproval(payload.getSender(), payload.getSenderUniqueId(), payload.getMessage(), payload.getReceiverId(), payload.getEvents(), payload.getUserType(), payload.getSourceApp());
                break;
            case PCR_TRANSFER:
                handlePcrTransfer(payload.getSender(), payload.getSenderUniqueId(), payload.getMessage(), payload.getReceiverId(), payload.getEvents(), payload.getUserType(), payload.getSourceApp());
                break;
            case JOB_ASSIGNED:
                handleJobAssigned(payload.getSender(), payload.getSenderUniqueId(), payload.getMessage(), payload.getReceiverId(), payload.getEvents(), payload.getUserType(), payload.getSourceApp());
                break;
            default:
                LOGGER.warn("Unhandled event type: {}", payload.getEvents());
        }
    }

    private void handleJobApplicationRequest(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {
        handleNotification(sender, senderUniqueId, message, receiverId, events, userType, sourceApp);
    }

    private void handleCustomServiceRequest(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {
        handleNotification(sender, senderUniqueId, message, receiverId, events, userType, sourceApp);
    }

    private void handleCustomServiceRequestStatus(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {
        handleNotification(sender, senderUniqueId, message, receiverId, events, userType, sourceApp);
    }

    private void handleInvitationRequest(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {
        handleNotification(sender, senderUniqueId, message, receiverId, events, userType, sourceApp);
    }

    private void handleJobInvitationRequestStatus(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {
        handleNotification(sender, senderUniqueId, message, receiverId, events, userType, sourceApp);
    }

    private void handleWarrantTransfer(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {
        handleNotification(sender, senderUniqueId, message, receiverId, events, userType, sourceApp);
    }

    private void handleOfferRequest(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {
        handleNotification(sender, senderUniqueId, message, receiverId, events, userType, sourceApp);
    }

    private void handleOfferRequestStatus(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {
        handleNotification(sender, senderUniqueId, message, receiverId, events, userType, sourceApp);
    }

    private void handleServiceOrderPayment(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {
        handleNotification(sender, senderUniqueId, message, receiverId, events, userType, sourceApp);
    }

    private void handlePaymentReleased(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {
        handleNotification(sender, senderUniqueId, message, receiverId, events, userType, sourceApp);
    }

    private void handleGuarantorRequest(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {
        handleNotification(sender, senderUniqueId, message, receiverId, events, userType, sourceApp);
    }

    private void handleGuarantorRequestStatus(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {
        handleNotification(sender, senderUniqueId, message, receiverId, events, userType, sourceApp);
    }

    private void handleServiceOrderApprovalRequest(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {
        handleNotification(sender, senderUniqueId, message, receiverId, events, userType, sourceApp);
    }

    private void handleServiceOrderApproval(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {
        handleNotification(sender, senderUniqueId, message, receiverId, events, userType, sourceApp);
    }

    private void handlePcrTransfer(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {
        handleNotification(sender, senderUniqueId, message, receiverId, events, userType, sourceApp);
    }

    private void handleJobAssigned(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {
        handleNotification(sender, senderUniqueId, message, receiverId, events, userType, sourceApp);
    }


    private void handleNotification(String sender, String senderUniqueId, String message, String receiverId, Events events, UserType userType, SourceApp sourceApp) {

        NotificationPayload payload = new NotificationPayload(sender, senderUniqueId, message, receiverId, events, userType, sourceApp);
        LOGGER.info("Handling {} request: {}", events, payload);

        BaseResponse response = createNotification(payload);
        LOGGER.info("Received response from createNotification: {}", response);

        if (response.getStatusCode() != HttpServletResponse.SC_OK) {
            throw new NotificationNotSavedException("Failed to save notification to database");
        }

    }

    private BaseResponse createNotification(NotificationPayload payload) {

        BaseResponse response = new BaseResponse();

        Notification notification = new Notification();

        notification.setSender(payload.getSender());
        notification.setSenderUniqueId(payload.getSenderUniqueId());
        notification.setMessage(payload.getMessage());
        notification.setReceiver(payload.getReceiverId());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setEvents(payload.getEvents());
        notification.setUserType(payload.getUserType());
        notification.setSourceApp(payload.getSourceApp());

        try {

            // Save to the notification db
            Notification savedNotifications = notificationRepo.save(notification);

            // Extract the notification id and the creation time
            payload.setCreatedAt(savedNotifications.getCreatedAt());
            payload.setId(savedNotifications.getId());

            //Construct Redis key
            String redisKey = REDIS_HASH_KEY + payload.getReceiverId();

            // Cache notification to Redis server
            redisTemplate.opsForList().leftPush(redisKey, payload);

            response.setData(notification);
            response.setDescription("Notification created successfully");
            response.setStatusCode(HttpServletResponse.SC_OK);

        } catch (Exception ex) {
            LOGGER.error("Failed to save notification to database: {}", ex.getMessage());
            response.setDescription("Failed to save notification to database: " + ex.getMessage());
            response.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return response;
    }

}