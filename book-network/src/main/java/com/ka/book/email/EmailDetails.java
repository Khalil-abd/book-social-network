package com.ka.book.email;

public record EmailDetails(
        String to,
        String username,
        EmailTemplateName emailTemplate,
        String confirmationUrl,
        String activationCode,
        String subject
) {}