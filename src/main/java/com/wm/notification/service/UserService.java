package com.wm.notification.service;

public interface UserService {

    String getUserName(String wmUniqueId);

    boolean existsByWmUniqueId(String wmUniqueId);

    String getUniqueIdById(Long id);

    String getUserProfilePictureUrl(String wmUniqueId);
}