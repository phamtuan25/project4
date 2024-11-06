import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import $ from 'jquery';
import 'bootstrap';
import { BookingDetail } from '../booking-detail-manager/booking-detail-manager.component';
import { AdminService } from '../admin.service';
@Component({
  selector: 'app-booking-manager',
  templateUrl: './booking-manager.component.html',
  styleUrl: './booking-manager.component.css'
})
export class BookingManagerComponent implements OnInit {
  bookings: Booking[] = [];
  filterBookings: Booking[] = [];
  bookingId: number = 0;
  bookingDetailResponses: BookingDetail[] = [];
  
  constructor(public admin: AdminComponent, private adminService: AdminService) { }
  ngOnInit(): void {
    this.admin.pageTitle = 'Booking Management';
  }

 


}

export interface Booking {
  bookingId: number;
  userBookingResponse: UserBookingResponse;
  bookingDetailResponses: BookingDetail[];
  createdAt: Date; 
  updatedAt: Date; 
  status: string; 
  deposit: number;
  totalAmount: number;
}
export interface UserBookingResponse {
  userId: number;
  fullName: string;
  email: string; 
  phoneNumber: string; 
  address: string; 
}







