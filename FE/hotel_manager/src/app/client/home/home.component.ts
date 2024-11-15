import { Component, OnInit } from '@angular/core';
import { ClientComponent } from '../client.component';
import { ClientService } from '../client.service';
import { ActivatedRoute, Router } from '@angular/router';
declare var $: any
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  checkin: string = '';
  checkout: string = '';
  roomType: string = '';
  rooms: any[] = [];
  totalPages: number = 0;
  currentPage: number = 1;

  roomTypes: string[] = ['Single Room', 'Double Room', 'Suite']; // Các loại phòng có sẵn

  constructor(private clientService: ClientService, private router: Router) {}

  // Hàm kiểm tra phòng có sẵn
  checkAvailability() {
    if (!this.checkin || !this.checkout || !this.roomType) {
      alert('Please fill out all fields');
      return;
    }

    // Gọi API để lấy phòng có sẵn từ ClientService
    this.clientService.getAvailableRooms(this.checkin, this.checkout, this.roomType, this.currentPage)
      .subscribe(response => {
        this.rooms = response.rooms; // Dữ liệu phòng trả về từ API
        this.totalPages = response.totalPages; // Tổng số trang để phân trang
      }, error => {
        console.error('Error fetching room availability', error);
      });
  }

  // Chuyển trang khi người dùng chọn phân trang
  goToPage(page: number) {
    if (page > 0 && page <= this.totalPages) {
      this.currentPage = page;
      this.checkAvailability();
    }
  }
}