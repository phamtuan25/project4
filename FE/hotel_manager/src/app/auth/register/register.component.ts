import { Component } from '@angular/core';
import { AuthService} from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  firstName: string = '';
  lastName: string = '';
  address: string = '';
  email: string = '';
  phoneNumber: string = '';
  password: string = '';

  errors:any= [];

  constructor(private authService: AuthService, private router: Router) { }

  onSubmit() {
    this.authService.register(this.firstName, this.lastName, this.address, this.email, this.phoneNumber, this.password).subscribe(
      response => {
        console.log('Registration successful:', response);
        this.router.navigate(['/login']);
      },
      error => {
        this.errors = [];
        error.error.forEach((element:any) => {
          this.errors.push(element);
        });
        console.log(this.errors);
        console.error('Registration failed:', error);
      }
    );
  }

  findErrors(key:string){
    return this.errors.find((error:any)=>error.key==key).message;
  }
}
