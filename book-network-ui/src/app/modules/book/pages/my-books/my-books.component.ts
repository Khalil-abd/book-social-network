import {Component, OnInit} from '@angular/core';
import {BookCardComponent} from '../../components/book-card/book-card.component';
import {PageResponseBookResponse} from '../../../../services/models/page-response-book-response';
import {BookService} from '../../../../services/services/book.service';
import {RouterLink} from '@angular/router';
import {BookResponse} from '../../../../services/models/book-response';

@Component({
  selector: 'app-my-books',
  standalone: true,
  imports: [
    BookCardComponent,
    RouterLink
  ],
  templateUrl: './my-books.component.html',
  styleUrl: './my-books.component.scss'
})
export class MyBooksComponent implements OnInit{

  bookResponse: PageResponseBookResponse = {};
  page: number = 0;
  size: number = 4;
  private _lastPage: number = 0;
  private _isLastPage: boolean = false;

  constructor(
    private bookService: BookService
  ) {
  }

  get isLastPage() :boolean{
    return this._isLastPage;
  }

  ngOnInit(): void {
    this.findAllBooks();
  }

  private findAllBooks() {
    this.bookService.findAllBooksByOwner({
      page: this.page,
      size: this.size,
    }).subscribe({
      next: (books) => {
        this.bookResponse = books;
        this._lastPage = books.totalPages as number -1;
        this._isLastPage = books.last as boolean;
      }
    });
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBooks();
  }

  goToPreviousPage() {
    this.page -= 1;
    this.findAllBooks();
  }

  goToLastPage() {
    this.page = this._lastPage;
    this.findAllBooks();
  }

  goToNextPage() {
    this.page+= 1;
    this.findAllBooks();
  }

  goToPage(page: number) {
    if(this.page !== page){
      this.page = page;
      this.findAllBooks();
    }
  }

  archiveBook(book: BookResponse) {
    this.bookService.updateArchivedStatus({
      'book-id': book.id as number
    }).subscribe({
      next: () =>{

      },
      error: (err) =>{
        console.log(err);
      }
    });
  }

  shareBook(book: BookResponse) {
    this.bookService.updateShareableStatus({
      'book-id': book.id as number
    }).subscribe({
      next: () =>{

      },
      error: (err) =>{
        console.log(err);
      }
    });
  }

  editBook(book: BookResponse) {
    console.log(book);
  }
}
