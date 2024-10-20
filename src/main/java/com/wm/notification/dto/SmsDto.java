package com.wm.notification.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SmsDto {

    private String message;

    private String sender;

    private List<String> receiver;
}