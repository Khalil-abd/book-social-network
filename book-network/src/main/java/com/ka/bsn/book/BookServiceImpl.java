package com.ka.bsn.book;

import com.ka.bsn.exception.OperationNotPermitedException;
import com.ka.bsn.common.PageResponse;
import com.ka.bsn.file.FileStorageService;
import com.ka.bsn.history.BookTransactionHistory;
import com.ka.bsn.history.BookTransactionHistoryRepository;
import com.ka.bsn.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.ka.bsn.book.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository transactionHistoryRepository;
    private final FileStorageService fileStorageService;


    private static final String BOOK_NOT_FOUND = "No book found with ID:: ";
    private static final String CREATED_DATE = "createdDate";

    @Override
    public Long save(BookRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book;
        if(request.id() != null){
            book = bookRepository.findById(request.id())
                    .orElseThrow(()->new EntityNotFoundException(BOOK_NOT_FOUND + request.id()));
            book = bookMapper.updateBook(book, request);
        }else{
            book = bookMapper.toBook(request);
        }
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    @Override
    public BookResponse findById(Long bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND + bookId));
    }

    @Override
    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by(CREATED_DATE).descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    @Override
    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by(CREATED_DATE).descending());
        Page<Book> books = bookRepository.findAll(withOwnerId(user.getId()), pageable);
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    @Override
    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by(CREATED_DATE).descending());
        Page<BookTransactionHistory> allBorrowedBooks = transactionHistoryRepository
                .findAllBorrowedBooks(pageable, user.getId());
        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    @Override
    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by(CREATED_DATE).descending());
        Page<BookTransactionHistory> allBorrowedBooks = transactionHistoryRepository
                .findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    @Override
    public Long updateShareableStatus(Long bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND + bookId));
        User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermitedException("You cannot update others books shareable status");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    @Override
    public Long updateArchivedStatus(Long bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND + bookId));
        User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermitedException("You cannot update others books archived status");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    @Override
    public Long borrowBook(Long bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND + bookId));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermitedException("The requested book cannot be borrowed since it is archived or not shareable");
        }
        User user = (User) connectedUser.getPrincipal();
        if(Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermitedException("You cannot borrow your own book");
        }
        final boolean isBorrowed = transactionHistoryRepository.isAlreadyBorrowed(bookId);
        if(isBorrowed){
            throw new OperationNotPermitedException("The requested book is already borrowed");
        }
        BookTransactionHistory bookTransaction = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return transactionHistoryRepository.save(bookTransaction).getId();
    }

    @Override
    public Long returnBook(Long bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND + bookId));
        User user = (User) connectedUser.getPrincipal();
        if(Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermitedException("You cannot borrow or return your own book");
        }
        final boolean isBorrowed = transactionHistoryRepository.isAlreadyBorrowed(bookId);
        if(!isBorrowed){
            throw new OperationNotPermitedException("The book is not borrowed");
        }

        BookTransactionHistory bookTransaction = transactionHistoryRepository
                .findBorrowedBookByUserId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermitedException("You book is not returned yet. " +
                        "You cannot approve its return") );

        bookTransaction.setReturned(true);
        return transactionHistoryRepository.save(bookTransaction).getId();
    }

    @Override
    public Long approveReturnBook(Long bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND + bookId));
        User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermitedException("You cannot approve the return of a book you do not own");
        }
        BookTransactionHistory bookTransaction = transactionHistoryRepository
                .findReturnedBookToApprove(bookId)
                .orElseThrow(() -> new OperationNotPermitedException("You did not borrow this book") );

        bookTransaction.setReturnApproved(true);
        return transactionHistoryRepository.save(bookTransaction).getId();
    }

    @Override
    public void uploadBookCover(MultipartFile file, Long bookId, Authentication connectedUser) {
        if(file == null){
            throw new OperationNotPermitedException("No cover picture provided!");
        }
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND + bookId));
        User user = (User) connectedUser.getPrincipal();
        var bookCover = fileStorageService.saveFile(file, user.getId());
        book.setBookCover(bookCover);
        bookRepository.save(book);
    }
}
