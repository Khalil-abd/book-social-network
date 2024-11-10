import {Component, OnInit} from '@angular/core';
import {NgIf} from '@angular/common';
import {BookService} from '../../../../services/services/book.service';
import {PageResponseBorrowedBookResponse} from '../../../../services/models/page-response-borrowed-book-response';
import {BorrowedBookResponse} from '../../../../services/models/borrowed-book-response';

@Component({
  selector: 'app-returned-book-list',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './returned-book-list.component.html',
  styleUrl: './returned-book-list.component.scss'
})
export class ReturnedBookListComponent implements OnInit{

  message: string = '';
  level: string = '';
  returnedBooks: PageResponseBorrowedBookResponse = {};
  page: number = 0;
  size: number = 4;
  private _lastPage: number = 0;
  private _isLastPage: boolean = true;

  get isLastPage() {
    return this._isLastPage;
  }

  constructor(
    private bookService: BookService
  ) {
  }

  ngOnInit(): void {
    this.findAllReturnedBooks();
  }

  private findAllReturnedBooks() {
    this.message = '';
    this.level = '';
    this.bookService.findAllReturnedBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (books)=>{
        this.returnedBooks = books;
      }
    })
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllReturnedBooks();
  }

  goToPreviousPage() {
    this.page -= 1;
    this.findAllReturnedBooks();
  }

  goToLastPage() {
    this.page = this._lastPage;
    this.findAllReturnedBooks();
  }

  goToNextPage() {
    this.page += 1;
    this.findAllReturnedBooks();
  }

  goToPage(page: number) {
    if (this.page !== page) {
      this.page = page;
      this.findAllReturnedBooks();
    }
  }

  approveReturnBook(book: BorrowedBookResponse) {
    if(!book.returned){
      this.level = 'error';
      this.message = 'The book is not yet returned';
      return;
    }
    if(book.returnApproved){
      this.level = 'warn';
      this.message = 'The book return is already approved';
      return;
    }
    this.bookService.approveReturnBook({
      'book-id': book.id as number
    }).subscribe({
      next: () => {
        book.returnApproved = true;
        this.level = 'success';
        this.message = 'Book return approved successfully';
      },
      error: (err) =>{
        this.level = 'error';
        this.message = 'Failed to approve book return';
      }
    });
  }
}
