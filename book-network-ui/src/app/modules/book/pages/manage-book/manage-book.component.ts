import { Component } from '@angular/core';
import {BookRequest} from '../../../../services/models/book-request';
import {FormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {BookService} from '../../../../services/services/book.service';

@Component({
  selector: 'app-manage-book',
  standalone: true,
  imports: [
    FormsModule,
    RouterLink
  ],
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss'
})
export class ManageBookComponent {

  bookRequest: BookRequest = {
    authorName: '',
    isbn: '',
    synopsis: '',
    title: ''
  };
  errorMsg: Array<string> = [];
  selectedBookCover: any;
  selectedPicture: string = '';
  defaultPicture: string = 'assets/images/product-not-found.png'

  constructor(
    private readonly bookService: BookService,
    private readonly router: Router
  ) {
  }

  onFileSelected(event: any) {
    this.selectedBookCover = event.target.files[0];
    console.log(this.selectedBookCover);
    if(this.selectedBookCover){
      const reader = new FileReader();
      reader.onload= () => {
        this.selectedPicture = reader.result as string;
      };
      reader.readAsDataURL(this.selectedBookCover);
    }
  }

  saveBook() {
    this.bookService.saveBook({
      body: this.bookRequest
    }).subscribe({
      next: (bookId) =>{
        this.bookService.uploadBookCover({
          'book-id': bookId,
          body: this.selectedBookCover
        }).subscribe({
          next: ()=> {
            this.router.navigate(['/books/my-books']);
          },
          error: (err) =>{
            this.errorMsg = [];
            this.errorMsg.push(err.error.error);
          }
        });
      },
      error: (err) =>{
        console.log(err);
        this.errorMsg = [];
        this.errorMsg = err.error.validationErrors;
      }
    })
  }
}
