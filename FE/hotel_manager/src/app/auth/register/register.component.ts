import { Component } from '@angular/core';
import { AuthService} from '../auth.service';
import { FormsModule } from '@angular/forms';

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

  constructor(private authService: AuthService) { }

  onSubmit() {
    this.authService.register(this.firstName, this.lastName, this.address, this.email, this.phoneNumber, this.password).subscribe(
      response => {
        console.log('Registration successful:', response);
        // Xử lý phản hồi thành công (ví dụ: chuyển hướng, hiển thị thông báo)
      },
      error => {
        console.error('Registration failed:', error);
        // Xử lý lỗi
      }
    );
  }
}
