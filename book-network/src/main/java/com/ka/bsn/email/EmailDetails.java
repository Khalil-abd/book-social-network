package com.ka.bsn.email;

public record EmailDetails(
        String to,
        String username,
        EmailTemplateName emailTemplate,
        String confirmationUrl,
        String activationCode,
        String subject
) {}