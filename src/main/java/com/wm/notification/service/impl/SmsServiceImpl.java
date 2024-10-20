package com.wm.notification.service.impl;

import com.africastalking.AfricasTalking;
import com.africastalking.sms.Recipient;
import com.wm.notification.config.AfricaTalkingConfig;
import com.wm.notification.dto.SmsDto;
import com.wm.notification.exception.DataNotPersistedException;
import com.wm.notification.exception.InvalidNotificationInputException;
import com.wm.notification.model.Sms;
import com.wm.notification.response.BaseResponse;
import com.wm.notification.respository.SmsRepository;
import com.wm.notification.service.SmsService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SmsServiceImpl implements SmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsServiceImpl.class);

    private final SmsRepository smsRepository;

    private final AfricaTalkingConfig africaTalkingConfig;

    private final AfricasTalking africasTalking;

    public SmsServiceImpl(SmsRepository smsRepository, AfricaTalkingConfig africaTalkingConfig, AfricasTalking africasTalking) {
        this.smsRepository = smsRepository;
        this.africaTalkingConfig = africaTalkingConfig;
        this.africasTalking = africasTalking;
    }

    @Override
    public BaseResponse send(SmsDto dto) {

        BaseResponse response = new BaseResponse();

        try {
            if (dto !=null) {
                sendMessage(dto);
                response.setDescription("SMS sent successfully");
                response.setStatusCode(HttpServletResponse.SC_OK);
            } else {
                LOGGER.error("SendSmsDto is null");
                throw new InvalidNotificationInputException("SendSmsDto is null");
            }
        } catch (InvalidNotificationInputException ex) {
            LOGGER.error("Invalid argument: {}", ex.getMessage());
            response.setDescription("Failed to send SMS: " + ex.getMessage());
            response.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
        } catch (DataNotPersistedException ex) {
            LOGGER.error("Failed to save sms to the database", ex);
            response.setDescription("Failed to send SMS. An error occurred while saving to the database.");
            response.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }  catch (Exception ex) {
            LOGGER.error("Failed to send SMS: {}", ex);
            response.setDescription("Failed to send SMS: " + ex.getMessage());
            response.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    private void sendMessage(SmsDto dto) {

        save(dto);

        sendSms(dto);
    }

    private void sendSms(SmsDto smsDto) {

        try {

            africasTalking.initialize(
                    africaTalkingConfig.getUsername(),
                    africaTalkingConfig.getApiKey()
            );
            com.africastalking.SmsService sms = africasTalking.getService(africasTalking.SERVICE_SMS);
            String message = smsDto.getMessage();
            String[] recipient = smsDto.getReceiver().toArray(new String[0]);

            List<Recipient> responses = sms.send(message,null, recipient, false);

            for (Recipient response : responses) {
                if (response.status.equals("Success")) {
                    LOGGER.info("SMS sent successfully to " + response.number);
                } else {
                    LOGGER.warn("Failed to send SMS to " + response.number + ": " + response.status);
                }
             }
        } catch (IOException ex) {
            LOGGER.error("IOException occurred while sending SMS: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            LOGGER.error("An error occurred while sending SMS: " + ex.getMessage(), ex);
        }

    }

    private BaseResponse save(SmsDto dto) {
        BaseResponse response = new BaseResponse();
        try {
            Sms sms = new Sms();
            sms.setReceiver(dto.getReceiver());
            sms.setSender(dto.getSender());
            sms.setMessage(dto.getMessage());
            sms.setCreatedAt(LocalDateTime.now());
            smsRepository.save(sms);

            response.setData(sms);
            response.setDescription("Saved sms to the database");
            response.setStatusCode(HttpServletResponse.SC_OK);
        } catch (DataAccessException ex) {
            LOGGER.error("Failed to save sms to the database", ex.getMessage());
            throw new DataNotPersistedException("An error occurred. Unable to save sms to the database");
        } catch (Exception ex) {
            LOGGER.error("Failed to save sms to the database", ex.getMessage());
            throw new RuntimeException("An error occurred. Unable to save sms to the database");
        }
        return response;
    }

    private void inputValidation(SmsDto dto) {
        if (dto.getReceiver() ==null && dto.getMessage() ==null) {
            throw new InvalidNotificationInputException("Phone number or message cannot be null");
        }
    }
}