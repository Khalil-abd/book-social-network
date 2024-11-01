package com.ka.bsn.book;


public record BorrowedBookResponse(
        Long id,
        String title,
        String authorName,
        String isbn,
        double rate,
        boolean returned,
        boolean returnApproved
)
{}
