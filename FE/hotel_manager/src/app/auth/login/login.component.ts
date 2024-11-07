import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { AppComponent } from '../../app.component';
import { HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { ConfigService } from '../../../config/config.service';

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

  constructor(private authService: AuthService, private config: ConfigService, private router: Router) { }
  onSubmit() {
    this.authService.login(this.user).subscribe(
      response => {
        const token = '"' + response.token + '"'
        this.config.setToken(token);
        this.config.setEmail(response.message);
        console.log('Login successful:', response.message);
        this.router.navigate(['/']);
      },
      error => {
        this.errors = [];
        if(error.error.length>1){
          error.error.forEach((element:any) => {
            this.errors.push(element);
          });
        } else{
          this.errors.push({key: 'password', message: 'Invalid Password'});
        }
        console.error('Login failed:', error);
      }
    );
  }

  findErrors(key:string){
    return this.errors.find((error:any)=>error.key==key)?.message;
  }
}
