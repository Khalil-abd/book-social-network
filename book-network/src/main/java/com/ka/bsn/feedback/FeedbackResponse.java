package com.ka.bsn.feedback;

public record FeedbackResponse(
        Double note,
        String comment,
        boolean ownFeedback
) {
}
