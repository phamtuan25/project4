import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import { AdminService } from '../admin.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-booking-detail-manager',
  templateUrl: './booking-detail-manager.component.html',
  styleUrl: './booking-detail-manager.component.css'
})
export class BookingDetailManagerComponent implements OnInit {
  bookingDetail: BookingDetail[] = [];
  bookingDetailId: number = 0;
  bookingId: number = 0;
  errors: any[] = [];
  roomNumber: string = "";
  checkIn: string = "";
  checkOut: string = "";
  status: string = "";
  specialRequests: string = "";
  price: number = 0;
  isShowAddPopup: Boolean = false;
  isShowEditPopup: Boolean = false;
  pageSize: number = 10;
  currentPage: number = 1;
  totalPages: number = 0;
  keyword: string = '';
  currentBookingPage: number = 0;
  constructor(
    public admin: AdminComponent,
    private adminService: AdminService,
    private route: ActivatedRoute,
    private router: Router , 
  ) { }
  ngOnInit(): void {
    this.admin.pageTitle = 'Booking Detail Management';

    this.bookingId = +this.route.snapshot.paramMap.get('bookingId')!;
    if (isNaN(this.bookingId)) {
      console.error('Invalid bookingId');
    }
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras.state) {
      this.currentBookingPage = navigation.extras.state['currentPage'];
    }
    this.getBookingDetails(this.currentPage, this.pageSize, this.keyword);
  }

  goBack(): void {
    console.log(this.currentPage)
    if (this.currentPage) {
      this.router.navigate(['/admin/booking-manager'], {
        queryParams: { page: this.currentBookingPage }
      });
    } else {
      this.router.navigate(['/admin/booking-manager']);
    }
  }
  getBookingDetails(page: number, size: number, keyword: string): void {
    this.adminService.getBookingDetail(this.bookingId, page - 1, size, keyword).subscribe(
      (response: any) => {
        this.bookingDetail = response.content;
        this.totalPages = response.totalElements;
        this.totalPages = Math.ceil(this.totalPages / this.pageSize);
      },
      (error) => {
        console.error('Error fetching booking details', error);
        this.bookingDetail = [];
        this.errors.push(error);
      }
    );
  }

  // Chuyển trang
  goToPage(page: number): void {
    if (page < 1 || page > Math.ceil(this.totalPages / this.pageSize)) return;
    this.currentPage = page;
    this.getBookingDetails(this.currentPage, this.pageSize, this.keyword);
  }

  // Tìm kiếm booking
  searchBookings(): void {
    const input: string = (document.getElementById('searchBookingsInput') as HTMLInputElement).value.trim();
    this.keyword = input;
    this.currentPage = 1;
    this.getBookingDetails(this.currentPage, this.pageSize, this.keyword);
  }

  handleKeyPress(event: any) {
    if (event.key === 'Enter') {
      this.searchBookings();
    }
  }

  openEditBookingDetail(bookingDetail: BookingDetail) {
    this.bookingDetailId = bookingDetail.bookingDetailId
    this.roomNumber = bookingDetail.roomNumber
    this.checkIn = bookingDetail.checkIn;
    this.checkOut = bookingDetail.checkOut
    this.status = bookingDetail.status
    this.specialRequests = bookingDetail.specialRequests
    this.price = bookingDetail.price
    this.isShowEditPopup = true;
    this.openPopup();
  }

  onSubmitEdit(form: NgForm) {
    if (form.valid) {
      this.adminService.editBookingDetail(this.bookingDetailId, this.checkIn, this.checkOut, this.status, this.specialRequests, this.price).subscribe(
        response => {
          alert("Edit Success!");
          this.getBookingDetails(this.currentPage, this.pageSize, this.keyword);
          this.resetFormData();
        },
      );
    }
  }
  resetFormData() {
    this.checkIn = "";
    this.checkOut = "";
    this.status = "";
    this.specialRequests = "";
    this.price = 0;
    this.isShowAddPopup = false;
    this.isShowEditPopup = false;
    this.closePopup();
  }
  isObject(value: any) {
    return typeof value === 'object' && value !== null && !Array.isArray(value);
  }
  openPopup() {
    document.body.style.paddingRight = '17px'
    document.body.classList.add('modal-open')

  }
  closePopup() {
    document.body.style.paddingRight = '';
    document.body.classList.remove('modal-open')
  }
}


export interface BookingDetail {
  bookingDetailId: number;
  bookingId: number;
  roomId: number;
  roomNumber: string;
  checkIn: string;
  checkOut: string;
  createdAt: string;
  updatedAt: string;
  status: string;
  specialRequests: string;
  price: number;
}