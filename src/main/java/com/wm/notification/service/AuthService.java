package com.wm.notification.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AuthService {

    String getAccessToken(Authentication authentication);

    Authentication getAuthentication();

    Jwt getJwtFromAuthentication(Authentication authentication);

    String getWmUniqueIdFromJwt(Jwt jwt);

    String getFullNameFromJwt(Jwt jwt);
}