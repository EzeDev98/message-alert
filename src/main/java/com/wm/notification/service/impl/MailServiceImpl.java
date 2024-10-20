package com.wm.notification.service.impl;

import com.wm.notification.dto.MailDto;
import com.wm.notification.exception.EmailNotFoundException;
import com.wm.notification.exception.WmUniqueIdNotFoundException;
import com.wm.notification.model.Mail;
import com.wm.notification.response.BaseResponse;
import com.wm.notification.respository.MailRepository;
import com.wm.notification.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    private final MailRepository repository;

    private final JavaMailSender javaMailSender;

    private String sender;

    public MailServiceImpl(MailRepository repository, JavaMailSender javaMailSender, String sender) {
        this.repository = repository;
        this.javaMailSender = javaMailSender;
        this.sender = sender;
    }

    @Override
    public BaseResponse sendMailMessage(MailDto dto) {
        BaseResponse response = new BaseResponse();
        try {
            String mail = "";
            if (mail !=null) {
                createMail(dto);
                sendMail(dto);
                response.setData(dto);
                response.setDescription("Mail created and sent successfully");
                response.setStatusCode(HttpServletResponse.SC_OK);
            } else {
                LOGGER.warn("Email address not found.");
                response.setDescription("Email address not found.");
                response.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }catch (EmailNotFoundException ex) {
            LOGGER.error("Email address not found: {}", ex.getMessage());
            response.setDescription("Email address not found");
            response.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            LOGGER.error("An error occurred while sending mail: {}", ex.getMessage());
            response.setDescription("An error occurred while sending mail");
            response.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    private BaseResponse sendMail(MailDto dto) {
        BaseResponse response = new BaseResponse();
//        String receiverEmail = userService.getUserEmailFromJwt();
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom(sender);
//            message.setTo(receiverEmail);
            message.setSubject(dto.getSubject());
            message.setText(dto.getMessage());

            javaMailSender.send(mimeMessage);
            response.setData(message);
            response.setDescription("Mail sent successfully");
            response.setStatusCode(HttpServletResponse.SC_OK);
        } catch (MessagingException ex) {
            LOGGER.warn("Email not sent");
            response.setDescription("Unable to send email");
            response.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }catch (Exception ex) {
            LOGGER.error("An error occurred while sending mail: {}", ex.getMessage());
            response.setDescription("An error occurred while sending mail");
            response.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    private BaseResponse createMail(MailDto dto) {

        BaseResponse response = new BaseResponse();

        try {
            Mail mail = new Mail();
            mail.setSubject(dto.getSubject());
            mail.setMessage(dto.getMessage());
            mail.setId(mail.getId());
            mail.setSender(sender);
//            sendMail.setReceiver(userService.getUserNameFromJWTToken());
            mail.setCreatedAt(LocalDateTime.now());
//            String uniqueId = userService.getWmUniqueIdFromJWTToken();
//            sendMail.setWmUniqueId(uniqueId);
//            String mail = userService.getUserEmailFromJwt();
//            sendMail.setEmail(mail);
            repository.save(mail);

            response.setData(mail);
            response.setDescription("Email created successfully");
            response.setStatusCode(HttpServletResponse.SC_OK);
        } catch (WmUniqueIdNotFoundException ex) {
            LOGGER.error("Unable to retrieve wmUniqueId: {}", ex.getMessage());
            response.setDescription("Failed to create email");
            response.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }catch (EmailNotFoundException ex) {
            LOGGER.error("Unable to retrieve email: {}", ex.getMessage());
            response.setDescription("Failed to create email");
            response.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }  catch (Exception ex) {
            LOGGER.error("Error occurred while saving email: {}", ex.getMessage());
            response.setDescription("Failed to save email");
            response.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}