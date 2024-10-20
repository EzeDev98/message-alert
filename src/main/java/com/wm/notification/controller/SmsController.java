package com.wm.notification.controller;

import com.wm.notification.dto.SmsDto;
import com.wm.notification.response.BaseResponse;
import com.wm.notification.service.SmsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("${app.title}")
@Tag(name = "SMS API", description = "Resource for managing sms notifications")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

//    @PostMapping("/sms")
//    public BaseResponse response (@RequestBody SmsDto smsDto) {
//        BaseResponse response = smsService.send(smsDto);
//        return response;
//    }
}