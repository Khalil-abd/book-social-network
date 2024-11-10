import {Component} from '@angular/core';
import {RegistrationRequest} from '../../services/models/registration-request';
import {FormsModule} from '@angular/forms';
import {NgIf} from '@angular/common';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../services/services/authentication.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    FormsModule,
    NgIf
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  registerRequest: RegistrationRequest = {
    email: "",
    firstName: "",
    lastName: "",
    password: ""
  };
  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
  ) {
  }

  register() {
    this.errorMsg = [];
    this.authService.register(
      {body: this.registerRequest}).subscribe({
      next: (res) => {
        this.router.navigate(['activate-account']);
      },
      error: (err) => {
        console.log(err);
        this.errorMsg = err.error.validationErrors;
      }
    });
  }

  login() {
    this.router.navigate(['login'])
  }
}
