package com.ka.bsn.feedback;

import com.ka.bsn.book.Book;
import com.ka.bsn.book.BookRepository;
import com.ka.bsn.common.PageResponse;
import com.ka.bsn.exception.OperationNotPermitedException;
import com.ka.bsn.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService{

    private final FeedbackRepository feedbackRepository;
    private final BookRepository bookRepository;
    private final FeedbackMapper mapper;

    @Override
    public Long save(FeedbackRequest request, Authentication connectedUser) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: "+request.bookId()));
        User user = (User) connectedUser.getPrincipal();
        if(Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermitedException("You cannot give feedback on your own book");
        }
        Feedback feedback;
        if(request.id() != null){
            feedback = feedbackRepository.findById(request.id())
                    .orElseThrow(() -> new EntityNotFoundException("No feedback found with ID:: "+request.id()));
            feedback = mapper.updateFeedback(feedback, request);
        }else{
            feedback = mapper.toFeedback(request);
        }
        return feedbackRepository.save(feedback).getId();
    }

    @Override
    public PageResponse<FeedbackResponse> findAllBookFeedbacks(
            Long bookId,
            int page,
            int size,
            Authentication connectedUser
    ){
        Pageable pageable = PageRequest.of(page, size);
        User user = (User) connectedUser.getPrincipal();
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);
        List<FeedbackResponse> responses = feedbacks.stream()
                .map(f -> mapper.toFeedbackResponse(f, user.getId()))
                .toList();
        return new PageResponse<>(
                responses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
