package com.wm.notification.controller;

import com.wm.notification.dto.NotificationPayload;
import com.wm.notification.enums.SourceApp;
import com.wm.notification.enums.UserType;
import com.wm.notification.model.Notification;
import com.wm.notification.response.BaseResponse;
import com.wm.notification.respository.NotificationRepository;
import com.wm.notification.service.NotificationService;
import com.wm.notification.service.impl.listener.EventNotificationListener;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("${app.title}")
@Tag(name = "Notifications API", description = "Resource for managing notifications")
public class NotificationController {

    private final NotificationService notificationService;

    private final NotificationRepository notificationRepo;


    public NotificationController(NotificationService notificationService, NotificationRepository notificationRepo) {
        this.notificationService = notificationService;
        this.notificationRepo = notificationRepo;
    }

    @Operation(method = "GET", summary = "Retrieve all notifications.",
            description = "This endpoint retrieves notifications for all recipients")
    @GetMapping("/notification/all")
    ResponseEntity<?> getAllNotifications() {
        BaseResponse response = notificationService.getAllNotifications();
        if (response.getStatusCode() == HttpServletResponse.SC_OK) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(method = "GET", summary = "Retrieve all notifications.",
            description = "This endpoint retrieves notifications for a user")
    @GetMapping("/notification/user")
    ResponseEntity<?> getUserNotifications(@RequestParam("wmUniqueId") String wmUniqueId,
                                           @RequestParam("userType") UserType userType,
                                           @RequestParam("sourceApp") SourceApp sourceApp) {
        BaseResponse response = notificationService.getUserNotifications(wmUniqueId, userType, sourceApp);
        if (response.getStatusCode() == HttpServletResponse.SC_OK) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(method = "POST", summary = "Mark a notification message as read by its ID.",
            description = "This endpoint marks a notification message as read by its unique identifier, 'id'")
    @PostMapping("/notification/read/{id}")
    public void readMessage(@PathVariable Long id, @RequestParam("wmUniqueId") String wmUniqueId) {
        notificationService.readMessage(wmUniqueId, id);
    }

    @GetMapping("/notification/{receiver}")
    public List<Notification> getNotifications(@PathVariable String receiver) {
        return notificationRepo.findByReceiver(receiver);
    }

}