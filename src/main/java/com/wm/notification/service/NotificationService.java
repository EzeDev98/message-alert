package com.wm.notification.service;

import com.wm.notification.enums.SourceApp;
import com.wm.notification.enums.UserType;
import com.wm.notification.response.BaseResponse;

public interface NotificationService {

    void readMessage(String wmUniqueId, Long id);

    BaseResponse getAllNotifications();

    BaseResponse getUserNotifications(String wmUniqueId, UserType userType, SourceApp sourceApp);
}