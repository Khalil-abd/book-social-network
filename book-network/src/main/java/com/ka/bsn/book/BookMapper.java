package com.ka.bsn.book;

import com.ka.bsn.file.FileUtils;
import com.ka.bsn.history.BookTransactionHistory;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = FileUtils.class
)
public interface BookMapper {

    @Mapping(target = "archived", constant = "false")
    Book toBook(BookRequest request);

    @Mapping(target = "cover", expression = "java(FileUtils.readFileFromLocation(book.getBookCover()))")
    @Mapping(target = "owner", source = "owner.fullName")
    BookResponse toBookResponse(Book book);

    @Mapping(target = "id", source = "book.id")
    @Mapping(target = "title", source = "book.title")
    @Mapping(target = "authorName", source = "book.authorName")
    @Mapping(target = "isbn", source = "book.isbn")
    @Mapping(target = "rate", source = "book.rate")
    BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history);
}
