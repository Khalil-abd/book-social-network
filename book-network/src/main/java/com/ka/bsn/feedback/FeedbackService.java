package com.ka.bsn.feedback;

import com.ka.bsn.common.PageResponse;
import org.springframework.security.core.Authentication;

public interface FeedbackService {
    Long save(FeedbackRequest request, Authentication connectedUser);

    PageResponse<FeedbackResponse> findAllBookFeedbacks(Long bookId, int page, int size, Authentication connectedUser);
}
