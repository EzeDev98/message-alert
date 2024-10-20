package com.wm.notification.service.impl;

import com.wm.notification.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    public String getAccessToken(Authentication authentication) {
        if (authentication != null && authentication.getCredentials() instanceof Jwt) {
            Jwt jwtToken = (Jwt) authentication.getCredentials();
            return jwtToken.getTokenValue();
        }
        return null;
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Jwt getJwtFromAuthentication(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return jwtAuthenticationToken.getToken();
        }
        return null;
    }

    public String getWmUniqueIdFromJwt(Jwt jwt) {
        return jwt != null ? jwt.getClaimAsString("wm-unique-id") : null;
    }

    public String getClaimAsString(Jwt jwt, String claimName) {
        return jwt != null ? jwt.getClaim(claimName).toString() : null;
    }

    public String getFullNameFromJwt(Jwt jwt) {
        String givenName = getClaimAsString(jwt, "given_name");
        String familyName = getClaimAsString(jwt, "family_name");
        return (givenName != null ? givenName + " " : "") + (familyName != null ? familyName : "");
    }
}