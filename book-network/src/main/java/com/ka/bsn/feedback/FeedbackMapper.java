package com.ka.bsn.feedback;

import org.mapstruct.*;

import java.util.Objects;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = Objects.class
)
public interface FeedbackMapper {

    @Mapping(target = "book.id", source = "bookId")
    Feedback toFeedback(FeedbackRequest request);

    Feedback updateFeedback(@MappingTarget Feedback feedback, FeedbackRequest request);

    @Mapping(target = "ownFeedback", expression = "java(Objects.equals(feedback.getCreatedBy(), userId))")
    FeedbackResponse toFeedbackResponse(Feedback feedback, Long userId);
}
