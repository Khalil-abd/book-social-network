import {Component, OnInit} from '@angular/core';
import {BookRequest} from '../../../../services/models/book-request';
import {FormsModule} from '@angular/forms';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
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
export class ManageBookComponent implements OnInit{

  bookRequest: BookRequest = {
    authorName: '',
    isbn: '',
    synopsis: '',
    title: ''
  };
  errorMsg: Array<string> = [];
  selectedBookCover: any;
  selectedPicture: string = '';
  defaultPicture: string = 'assets/images/product-not-found.png';


  constructor(
    private readonly bookService: BookService,
    private readonly router: Router,
    private activatedRoute: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    const bookId = this.activatedRoute.snapshot.params['bookId'];
    if(bookId)
    {
      this.bookService.findBookById({
        'book-id': bookId
      }).subscribe({
        next: (book) =>{
          this.bookRequest = {
            id: book.id,
            title: book.title as string,
            isbn: book.isbn as string,
            synopsis: book.synopsis as string,
            shareable: book.shareable,
            authorName: book.authorName as string
          };
          if(book.cover){
            this.selectedPicture = 'data:image/png;base64, '+ book.cover;
          }
        }
      })
    }
  }

  onFileSelected(event: any) {
    this.selectedBookCover = event.target.files[0];
    if (this.selectedBookCover) {

      const reader = new FileReader();
      reader.onload = () => {
        this.selectedPicture = reader.result as string;
      };
      reader.readAsDataURL(this.selectedBookCover);
    }
  }

  saveBook() {
    this.errorMsg = [];
    this.bookService.saveBook({
      body: this.bookRequest
    }).subscribe({
      next: (bookId) =>{
        if(this.selectedBookCover){

          console.log("Loading new picture")
          this.bookService.uploadBookCover({
            'book-id': bookId,
            body: {
              file: this.selectedBookCover
            }
          }).subscribe({
            next: ()=> {
              this.router.navigate(['/books/my-books']);
            },
            error: (err) =>{
              this.errorMsg.push(err.error.error);
            }
          });
        }else{
          this.router.navigate(['/books/my-books']);
        }
      },
      error: (err) =>{
        console.log(err);
        this.errorMsg = err.error.validationErrors;
      }
    })
  }
}
