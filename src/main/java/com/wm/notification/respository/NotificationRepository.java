package com.wm.notification.respository;

import com.wm.notification.enums.SourceApp;
import com.wm.notification.enums.UserType;
import com.wm.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByReceiverAndSourceAppOrderByCreatedAtDesc(String wmUniqueId, SourceApp sourceApp);

    List<Notification> findAllByReceiverAndUserTypeAndSourceAppOrderByCreatedAtDesc(String wmUniqueId, UserType userType, SourceApp sourceApp);

    List<Notification> findByReceiver(String receiver);
}