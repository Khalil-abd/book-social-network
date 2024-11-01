package com.ka.bsn.book;

public record BookResponse(
        Long id,
        String title,
        String authorName,
        String isbn,
        String synopsis,
        String owner,
        double rate,
        byte[] bookCover,
        boolean archived,
        boolean shareable
) {
}
