package com.wm.notification.controller;

import com.wm.notification.dto.EmailRequestDto;
import com.wm.notification.service.EmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("${app.title}")
@Tag(name = "Mail API", description = "Resource for managing mail notifications")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public void sendEmail(@RequestBody EmailRequestDto emailRequest) {
        emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getText());
    }




}
