package com.wm.notification.service;

public interface EmailService {

    void sendEmail(String to, String subject, String text);

}
