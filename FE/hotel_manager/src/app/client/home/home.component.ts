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
export class HomeComponent implements OnInit {
  availableRooms: any[] = [];  // Danh sách phòng có sẵn
  roomTypes: any[] = [];  // Danh sách loại phòng
  errors: any[] = [];
  totalRooms: number = 0;
  pageSize: number = 4;
  currentPage: number = 1;
  totalPages: number = 0;
  checkin: string = '';  // Ngày giờ nhận phòng
  checkout: string = ''; // Ngày giờ trả phòng
  roomType: string = ''; // Loại phòng

  constructor(
    private clientService: ClientService,  // Sử dụng ClientService
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const page = params['page'];
      if (page) {
        this.currentPage = +page;
      }
      // Gọi API để lấy danh sách phòng có sẵn
      this.getRooms(this.currentPage, this.pageSize);
      // Lấy danh sách loại phòng khi component khởi tạo
      this.getRoomTypes();
    });
  }

  ngAfterContentInit(): void {
    const myCarousel = document.querySelector('#roomCarousel');
    const carousel = $(myCarousel).carousel();
  }

  // Lấy danh sách loại phòng từ API
  getRoomTypes(): void {
    this.clientService.getRoomTypes().subscribe(
      (data: any) => {
        this.roomTypes = data;
      },
      (error) => {
        console.error("Error fetching room types", error);
        this.errors.push(error);
      }
    );
  }

  // Lấy danh sách phòng có sẵn từ API với phân trang
  getRooms(page: number, size: number): void {
    this.clientService.getRoomsWithHomePage(page - 1, size, this.checkin, this.checkout, this.roomType).subscribe(
      (response: any) => {
        this.availableRooms = response.content;
        this.totalRooms = response.totalElements;
        this.totalPages = Math.ceil(this.totalRooms / this.pageSize);
      },
      (error) => {
        console.error("Error fetching available rooms", error);
        this.availableRooms = [];
        this.errors.push(error);
      }
    );
  }

  // Chuyển đến trang khác
  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;

    this.router.navigate([], {
      queryParams: { page: this.currentPage },
      queryParamsHandling: 'merge',
    });
  }

  // Kiểm tra phòng có sẵn
  checkAvailability(): void {
    this.getRooms(this.currentPage, this.pageSize);
  }
}

export interface Room {
  roomId: number;
  roomName: string;
  description: string;
  price: number;
  status: string;
  images: string[];
}