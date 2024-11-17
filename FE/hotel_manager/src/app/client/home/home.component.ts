import { Component, OnInit } from '@angular/core';
import { ClientComponent } from '../client.component';
import { ClientService } from '../client.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Room } from '../room/room.component';
declare var $: any
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  checkin: string = '';
  checkout: string = '';
  roomType: string = '';
  roomTypes: string[] = ['SINGLE', 'DOUBLE', 'FAMILY', 'DELUXE'];
  rooms: Room[] = [];
  errors: any[] = [];
  totalRooms: number = 0;

  minCheckInDate: string = '';
  minCheckOutDate: string = '';

  constructor(public client: ClientComponent, private clientService: ClientService, private router: Router,) {this.setMinDateTimes(); }
  ngOnInit(): void {
    this.client.pageTitle = 'Home Page';

    const roomType = 'FAMILY';
    const pageSize = 4;

    this.getRoomsHome(pageSize, roomType);
  }


  goToDetail(roomId: number): void {
    this.router.navigate(['/room-detail', roomId], {
      state: { fromPage: this.router.url }
    });
  }
  getRoomsHome(size: number, roomType: string): void {
    this.clientService.getRoomsHome(0, size, '', 'AVAILABLE', roomType).subscribe(
      (response: any) => {
        console.log('API response:', response);

        if (Array.isArray(response.content)) {
          console.log('Response is an array');
        } else {
          console.error('Response is not an array!');
        }
        this.rooms = response.content.filter((room: any) => room.roomType.toUpperCase() === roomType.toUpperCase());
        console.log('Filtered rooms:', this.rooms);
        if (this.rooms.length === 0) {
          console.log('There are no FAMILY rooms available.');
        }
      },
      (error) => {
        console.error("Error fetching rooms", error);
        this.rooms = [];
        this.errors.push(error);
      }
    );
  }

  setMinDateTimes() {
    const currentDate = new Date();
    this.minCheckInDate = currentDate.toISOString().slice(0, 16); // Đảm bảo là datetime-local

    // Nếu đã có checkin, set min cho checkout
    if (this.checkin) {
      this.setMinCheckOutDate();
    }
  }

  // Cập nhật min date cho checkout sau khi checkin
  setMinCheckoutDate() {
    if (this.checkin) {
      this.minCheckOutDate = this.checkin;
    }
  }

  // Kiểm tra thông tin phòng
  checkRoomAvailability(): void {
    // Reset lỗi mỗi khi người dùng nhấn nút submit
    this.errors = [];

    // Kiểm tra các trường input
    if (!this.checkin || !this.checkout || !this.roomType) {
      this.errors.push('Please fill in all fields.');
      return;
    }

    // Kiểm tra check-in không được là quá khứ
    const currentDate = new Date();
    const checkinDate = new Date(this.checkin);
    if (checkinDate < currentDate) {
      this.errors.push('Check-in time cannot be in the past.');
      return;
    }

    // Kiểm tra check-out phải lớn hơn hoặc bằng check-in
    const checkoutDate = new Date(this.checkout);
    if (checkoutDate < checkinDate) {
      this.errors.push('Checkout time must be on or after check-in time.');
      return;
    }

    // Nếu không có lỗi, tiếp tục chuyển hướng với các tham số
    if (this.errors.length === 0) {
      const params = {
        checkin: this.checkin,
        checkout: this.checkout,
        roomType: this.roomType
      };
      this.router.navigate(['/room-availability'], { state: { params: params } });
    }
  }
}

