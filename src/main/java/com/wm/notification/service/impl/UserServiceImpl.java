package com.wm.notification.service.impl;

import com.wm.notification.service.AuthService;
import com.wm.notification.service.UserService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.wm.notification.constants.ApiConstants.*;

@Service
public class UserServiceImpl implements UserService {

    private final RestTemplate restTemplate;

    private final AuthService authService;

    public UserServiceImpl(RestTemplate restTemplate, AuthService authService) {
        this.restTemplate = restTemplate;
        this.authService = authService;
    }

    @Override
    public String getUserName(String wmUniqueId) {

        HttpHeaders headers = new HttpHeaders();

        String url = GET_USER_NAME.replace("{wmUniqueId}", wmUniqueId);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class, wmUniqueId);

        return response.getBody();
    }

    @Override
    public boolean existsByWmUniqueId(String wmUniqueId) {

        HttpHeaders headers = new HttpHeaders();

        String url = EXIST_UNIQUE_ID.replace("{wmUniqueId}",  wmUniqueId);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.GET, entity, Boolean.class);

        return response.getBody() != null && response.getBody();
    }

    @Override
    public String getUniqueIdById(Long id) {

        HttpHeaders headers = getHeaders();

        String url = GET_WM_UNIQUE_ID.replace("{id}", id.toString());

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        return response.getBody();
    }

    @Override
    public String getUserProfilePictureUrl(String wmUniqueId) {

        HttpHeaders headers = new HttpHeaders();

        String url = USER_IMAGE.replace("{wmUniqueId}", wmUniqueId);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class, wmUniqueId);

        return response.getBody();
    }

    private HttpHeaders getHeaders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accessToken = authService.getAccessToken(authentication);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }
}