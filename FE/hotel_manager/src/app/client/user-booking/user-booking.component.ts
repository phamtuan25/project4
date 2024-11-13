import { Component } from '@angular/core';
import { User } from '../../admin/user-manager/user-manager.component';
import { Router } from '@angular/router';
import { ConfigService } from '../../../config/config.service';
import { ClientService } from '../client.service';
import { GlobalStateService } from '../../../config/global.stage.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-user-booking',
  templateUrl: './user-booking.component.html',
  styleUrls: ['./user-booking.component.css']
})
export class UserBookingComponent {
  bookings: any[] = [];  
  selectedBookingDetail: any = null;
  userLogin: User | null = null;
  private userSubscription!: Subscription;
  constructor(private router: Router, private config: ConfigService, private clientService: ClientService, private globalStageService: GlobalStateService) {}

  ngOnInit(): void {
    this.userSubscription = this.globalStageService.getUserStage().subscribe(user => {
      this.userLogin = user;
    });
    this.getBookings(this.userLogin?.userId);
  }
  ngOnDestroy() {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }

  getBookings(userId: number | undefined): void {
    this.clientService.getBookingByUser(userId).subscribe(
      (response: any) => {
        this.bookings = response;
        console.log('Bookings:', this.bookings);
      },
      (error) => {
        console.error('Error fetching bookings', error);
      }
    );
  }

  getBookingDetail(bookingId: number): void {
    const booking = this.bookings.find(b => b.bookingId === bookingId);
    if (booking) {
      this.selectedBookingDetail = booking; 
      console.log('Booking detail:', this.selectedBookingDetail);
    }
  }

  toggleBookingDetail(bookingId: string) {
    if (this.selectedBookingDetail && this.selectedBookingDetail.bookingId === bookingId) {
      this.selectedBookingDetail = null;
    } else {
      this.selectedBookingDetail = this.bookings.find(booking => booking.bookingId === bookingId);
    }
  }
}
