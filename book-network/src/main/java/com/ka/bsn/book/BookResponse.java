package com.ka.bsn.book;

public record BookResponse(
    String title,
    String authorName,
    String isbn,
    String synopsis,
    double rate,
    byte[] bookCover,
    boolean archived,
    boolean shareable
){}
