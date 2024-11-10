import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import $ from 'jquery';
import 'bootstrap';
import { AdminService } from '../admin.service';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
@Component({
  selector: 'app-booking-manager',
  templateUrl: './booking-manager.component.html',
  styleUrl: './booking-manager.component.css'
})
export class BookingManagerComponent implements OnInit {
  bookings: Booking[] = [];
  bookingId: number = 0;
  errors: any[] = [];
  email: string = '';
  status: string = '';
  deposit: number = 0;
  totalAmount: number = 0;
  totalBookings: number = 0;
  isShowAddPopup: Boolean = false;
  isShowEditPopup: Boolean = false;
  pageSize: number = 10;
  currentPage: number = 1;
  totalPages: number = 0;
  keyword: string = '';
  constructor(public admin: AdminComponent, private adminService: AdminService, private router: Router) { }
  ngOnInit(): void {
    this.admin.pageTitle = 'Booking Management';
    this.getBookings(this.currentPage, this.pageSize, this.keyword);
  }

  getBookings(page: number, size: number, keyword: string) {
    this.adminService.getBooking(page - 1, size, keyword).subscribe(
      (response: any) => {
        this.bookings = response.content;
        this.totalBookings = response.totalElements;
        this.totalPages = Math.ceil(this.totalBookings / this.pageSize);
      },
      (error) => {
        console.error("Error fetching bookings", error);
        this.bookings = [];
        this.errors.push(error);
      }
    );
  }
  goToPage(page: number): void {
    if (page < 1 || page > Math.ceil(this.totalBookings / this.pageSize)) return;
    this.currentPage = page;
    this.getBookings(this.currentPage, this.pageSize, this.keyword);
  }


  searchBookings(): void {
    const input: string = (document.getElementById('searchBookingsInput') as HTMLInputElement).value.trim();
    this.keyword = input;
    this.currentPage = 1;
    this.getBookings(this.currentPage, this.pageSize, this.keyword);
  }
  handleKeyPress(event: any) {
    if (event.key === 'Enter') {
      this.searchBookings();
    }
  }
  goToBookingDetail(bookingId: number){
    this.router.navigate(['/admin/booking-detail-manager', bookingId], {
      state: { currentPage: this.currentPage }
    });
  }
  openEditBooking(booking: Booking) {
    this.bookingId = booking.bookingId
    this.email = booking.userBookingResponse.email;
    this.status = booking.status
    this.deposit = booking.deposit
    this.totalAmount = booking.totalAmount
    this.isShowEditPopup = true;
    this.openPopup();
  }
  // submit booking đã edit
  onSubmitEdit(form: NgForm) {
    if(form.valid) {
      this.adminService.editBooking(this.bookingId, this.status, this.deposit, this.totalAmount).subscribe(
        response => {
          alert("Edit Success!");
          this.getBookings(this.currentPage , this.pageSize, this.keyword);
          this.resetFormData();
        },
      );
    }
  }
  resetFormData() {
    this.status = "";
    this.deposit = 0;
    this.totalAmount = 0;
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

export interface Booking {
  bookingId: number;
  userBookingResponse: UserBookingResponse;
  createdAt: Date; 
  updatedAt: Date; 
  status: string; 
  deposit: number;
  totalAmount: number;
}
export interface UserBookingResponse {
  email: string; 
}







