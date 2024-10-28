import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { AppComponent } from '../../app.component';
import { HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

interface User {
  email: string,
  password: string
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})

export class LoginComponent {
  user: User={
    email: '',
    password: ''
  }
  

  errors:any= [];

  constructor(private authService: AuthService, private app: AppComponent, private router: Router) { }
  onSubmit() {
    this.authService.login(this.user).subscribe(
      response => {
        this.app.setToken(response.token);
        this.app.httpHeader = new HttpHeaders({
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + this.app.getToken()
        });
        console.log('Login successful:', response);
        this.router.navigate(['/admin']);
      },
      error => {
        this.errors = [];
        error.error.forEach((element:any) => {
          this.errors.push(element);
        });
        console.log(this.errors);
        console.error('Login failed:', error);
      }
    );
  }

  findErrors(key:string){
    return this.errors.find((error:any)=>error.key==key).message;
  }
}
