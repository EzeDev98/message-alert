package com.wm.notification.controller;

import com.wm.notification.dto.MailDto;
import com.wm.notification.response.BaseResponse;
import com.wm.notification.service.MailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("${app.title}")
@Tag(name = "Mail API", description = "Resource for managing mail notifications")
public class MailController {

    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

//    @PostMapping("/mail")
//    public BaseResponse sendMail(@RequestBody MailDto mailDto) {
//        BaseResponse response = mailService.sendMailMessage(mailDto);
//        return response;
//    }
}
