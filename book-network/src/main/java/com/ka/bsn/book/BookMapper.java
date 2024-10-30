package com.ka.bsn.book;

import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookMapper {

    @Mapping(target = "archived", constant = "false")
    Book toBook(BookRequest request);

    @Mapping(target = "bookCover", ignore = true)
    BookResponse toBookResponse(Book book);
}
