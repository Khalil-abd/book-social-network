import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BookResponse} from '../../../../services/models/book-response';

@Component({
  selector: 'app-book-card',
  standalone: true,
  imports: [],
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss'
})
export class BookCardComponent {

  private _book: BookResponse = {};
  private _manage: boolean= false;
  private _bookCover: string | undefined;


  get manage(): boolean {
    return this._manage;
  }

  @Input()
  set manage(value: boolean) {
    this._manage = value;
  }

  get bookCover(): string | undefined {
    if(this._book.cover){
      return 'data:image/jpg;base64,'+this._book.cover;
    }
    return this._bookCover;
  }

  get book(): BookResponse {
    return this._book;
  }

  @Input()
  set book(value: BookResponse) {
    this._book = value;
  }

  @Output() private share: EventEmitter<BookResponse> = new EventEmitter();
  @Output() private archive: EventEmitter<BookResponse> = new EventEmitter();
  @Output() private addToWaitingList: EventEmitter<BookResponse> = new EventEmitter();
  @Output() private borrow: EventEmitter<BookResponse> = new EventEmitter();
  @Output() private edit: EventEmitter<BookResponse> = new EventEmitter();
  @Output() private details: EventEmitter<BookResponse> = new EventEmitter();

  onShowDetails() {
    this.details.emit(this._book);
  }

  onBorrow() {
    this.borrow.emit(this._book);
  }

  onAddToWaitingList() {
    this.addToWaitingList.emit(this._book);
  }

  onArchive() {
    this.archive.emit(this._book);
  }

  onShare() {
    this.share.emit(this._book);
  }

  onEdit() {
    this.edit.emit(this._book);
  }
}
