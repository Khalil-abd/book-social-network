import {Component, OnInit} from '@angular/core';
import {BookService} from '../../../../services/services/book.service';
import {PageResponseBookResponse} from '../../../../services/models/page-response-book-response';
import {BookCardComponent} from '../../components/book-card/book-card.component';

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [
    BookCardComponent
  ],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss'
})
export class BookListComponent implements OnInit{

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
    this.bookService.findAllBooks({
      page: this.page,
      size: this.size
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
}
