package com.ka.bsn.book;

import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    private BookSpecification(){}

    public static Specification<Book> withOwnerId(Long ownerId){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }
}
