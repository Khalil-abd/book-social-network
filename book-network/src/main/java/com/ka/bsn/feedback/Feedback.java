package com.ka.bsn.feedback;


import com.ka.bsn.book.Book;
import com.ka.bsn.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Feedback extends BaseEntity {
    private Double note;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
