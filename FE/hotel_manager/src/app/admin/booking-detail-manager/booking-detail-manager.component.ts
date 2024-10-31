import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';

@Component({
  selector: 'app-booking-detail-manager',
  templateUrl: './booking-detail-manager.component.html',
  styleUrl: './booking-detail-manager.component.css'
})
export class BookingDetailManagerComponent implements OnInit {
  constructor(public admin: AdminComponent) { }
  ngOnInit(): void {
    this.admin.pageTitle = 'Booking Detail Management';
  }
}

export interface BookingDetail {
  bookingDetailId: number;
  bookingId: number;
  roomId: number;
  checkIn: Date;
  checkOut: Date;
  createdAt: Date;
  updatedAt: Date;
  status: string;
  specialRequests: string;
  price: number;
}