package com.wm.notification.service;

import com.wm.notification.dto.MailDto;
import com.wm.notification.response.BaseResponse;

public interface MailService {

     BaseResponse sendMailMessage(MailDto dto);
}
