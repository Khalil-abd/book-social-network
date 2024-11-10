import {Component, OnInit} from '@angular/core';
import {PageResponseBorrowedBookResponse} from '../../../../services/models/page-response-borrowed-book-response';
import {NgIf} from '@angular/common';
import {BorrowedBookResponse} from '../../../../services/models/borrowed-book-response';
import {BookService} from '../../../../services/services/book.service';
import {FeedbackRequest} from '../../../../services/models/feedback-request';
import {FormsModule} from '@angular/forms';
import {RatingComponent} from '../../components/rating/rating.component';
import {RouterLink} from '@angular/router';
import {FeedbackService} from '../../../../services/services/feedback.service';

@Component({
  selector: 'app-borrowed-book-list',
  standalone: true,
  imports: [
    NgIf,
    FormsModule,
    RatingComponent,
    RouterLink
  ],
  templateUrl: './borrowed-book-list.component.html',
  styleUrl: './borrowed-book-list.component.scss'
})
export class BorrowedBookListComponent implements OnInit {
  borrowedBooks: PageResponseBorrowedBookResponse = {};
  feedbackRequest: FeedbackRequest = {
    bookId: 0,
    comment: '',
    note: 0
  };
  page: number = 0;
  size: number = 4;
  private _lastPage: number = 0;
  private _isLastPage: boolean = true;
  selectedBook: BorrowedBookResponse | undefined = undefined;

  get isLastPage() {
    return this._isLastPage;
  }

  constructor(
    private bookService: BookService,
    private feedbackService: FeedbackService
  ) {
  }

  ngOnInit(): void {
    this.findAllBorrowedBooks();
  }

  returnBorrowedBook(book: BorrowedBookResponse) {
    this.selectedBook = book;
    this.feedbackRequest.bookId = book.id as number;
  }

  private findAllBorrowedBooks() {
    this.bookService.findAllBorrowedBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (borrowedBooks) => {
        this.borrowedBooks = borrowedBooks;
        this._lastPage = borrowedBooks.totalPages as number - 1;
        this._isLastPage = borrowedBooks.last as boolean;
      }
    });
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBorrowedBooks();
  }

  goToPreviousPage() {
    this.page -= 1;
    this.findAllBorrowedBooks();
  }

  goToLastPage() {
    this.page = this._lastPage;
    this.findAllBorrowedBooks();
  }

  goToNextPage() {
    this.page += 1;
    this.findAllBorrowedBooks();
  }

  goToPage(page: number) {
    if (this.page !== page) {
      this.page = page;
      this.findAllBorrowedBooks();
    }
  }

  returnBook(withFeedback: boolean) {
    this.bookService.returnBook({
      'book-id': this.selectedBook?.id as number,
    }).subscribe({
      next: () =>{
        if( withFeedback) {
          this.giveFeedback();
        }
        this.selectedBook = undefined;
        this.findAllBorrowedBooks();
      }
    });
  }

  private giveFeedback() {
    this.feedbackService.saveFeedback({
      body: this.feedbackRequest
    }).subscribe({
      next: ()=>{
      }
    });
  }
}
