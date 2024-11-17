package com.ka.bsn.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.*;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendEmail(EmailDetails emailDetails) throws MessagingException {
        String templateName;
        if(emailDetails.emailTemplate() == null)
            templateName = "confirm_email";
        else
            templateName = emailDetails.emailTemplate().getName();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name()
        );
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", emailDetails.username());
        properties.put("confirmationUrl", emailDetails.confirmationUrl());
        properties.put("activationCode", emailDetails.activationCode());

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("abdellaouikhalil98@gmail.com");
        helper.setTo(emailDetails.to());
        helper.setSubject(emailDetails.subject());

        String template = templateEngine.process(templateName, context);

        helper.setText(template, true);
        mailSender.send(mimeMessage);
    }
}
