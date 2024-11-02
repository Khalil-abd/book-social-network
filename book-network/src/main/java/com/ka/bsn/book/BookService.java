package com.ka.bsn.book;

import com.ka.bsn.common.PageResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface BookService {
    Long save(BookRequest request, Authentication connectedUser);

    BookResponse findById(Long bookId);

    PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser);

    PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser);

    PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser);

    PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser);

    Long updateShareableStatus(Long bookId, Authentication connectedUser);

    Long updateArchivedStatus(Long bookId, Authentication connectedUser);

    Long borrowBook(Long bookId, Authentication connectedUser);

    Long returnBook(Long bookId, Authentication connectedUser);

    Long approveReturnBook(Long bookId, Authentication connectedUser);

    void uploadBookCover(MultipartFile file, Long bookId, Authentication connectedUser);
}
