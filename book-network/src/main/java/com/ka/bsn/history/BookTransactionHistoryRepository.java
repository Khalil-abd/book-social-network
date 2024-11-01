package com.ka.bsn.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Long> {

    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.user.id = :userId
            AND returnApproved = false
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Long userId);

    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.book.owner.id = :userId
            AND history.returned = true
            AND history.returnApproved = true
            """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Long userId);

    @Query("""
            SELECT (COUNT(*) > 0) AS isBorrowed
            FROM BookTransactionHistory history
            WHERE history.book.id = :bookId
            AND history.returnApproved = false
            """)
    boolean isAlreadyBorrowed(Long bookId);

    @Query("""
            SELECT transaction
            FROM BookTransactionHistory transaction
            WHERE transaction.book.id = :bookId
            AND transaction.user.id = :userId
            AND transaction.returned = false
            AND transaction.returnApproved = false
            """)
    Optional<BookTransactionHistory> findBorrowedBookByUserId(Long bookId, Long id);

    @Query("""
            SELECT transaction
            FROM BookTransactionHistory transaction
            WHERE transaction.book.id = :bookId
            AND transaction.returned = true
            AND transaction.returnApproved = false
            """)
    Optional<BookTransactionHistory> findReturnedBookToApprove(Long bookId);
}
