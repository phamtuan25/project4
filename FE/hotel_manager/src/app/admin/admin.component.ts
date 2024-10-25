import { Component } from '@angular/core';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent {
  title = 'hotel_manager';
  pageTitle = 'Quản Lý Người Dùng'
}

function signOut(): void {
  if (confirm("Bạn có chắc chắn muốn đăng xuất?")) {
      alert("Đã đăng xuất!");
      window.location.href = '/auth/login.html'; // Điều hướng đến trang đăng nhập
  }
}
