package com.ka.bsn.book;

import com.ka.bsn.common.PageResponse;
import org.springframework.security.core.Authentication;

public interface BookService {
    Long save(BookRequest request, Authentication connectedUser);

    BookResponse findById(Long bookId);

    PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser);

    PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser);

}
