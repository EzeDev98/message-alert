package com.wm.notification.exception;

public class InvalidNotificationInputException extends IllegalArgumentException{
    public InvalidNotificationInputException(String message) {
         super(message);
    }
}
