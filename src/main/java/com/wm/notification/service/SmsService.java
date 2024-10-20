package com.wm.notification.service;

import com.wm.notification.dto.SmsDto;
import com.wm.notification.response.BaseResponse;

public interface SmsService {

    BaseResponse send(SmsDto dto);
}
