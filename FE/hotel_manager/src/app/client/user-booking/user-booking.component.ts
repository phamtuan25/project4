import { Component } from '@angular/core';
import { User } from '../../admin/user-manager/user-manager.component';
import { Router } from '@angular/router';
import { ConfigService } from '../../../config/config.service';
import { ClientService } from '../client.service';
import { GlobalStateService } from '../../../config/global.stage.service';
import { Subscription } from 'rxjs';
import { ClientComponent } from '../client.component';

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
  constructor(private router: Router, private config: ConfigService, private clientService: ClientService, private client: ClientComponent) {}

  ngOnInit() {
    this.getUserLogin(this.config.getEmail());
  }
  getUserLogin(email: string | null) {
    this.clientService.getUserLogin(email).subscribe(
      (response: any) => {
        console.log(response)
        this.userLogin = response;
        this.getBookings(response.userId);
      },
      (error) => {
        console.error("Error fetching users", error);
      }
    )
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
    }
  }

  toggleBookingDetail(bookingId: string) {
    if (this.selectedBookingDetail && this.selectedBookingDetail.bookingId === bookingId) {
      this.selectedBookingDetail = null;
    } else {
      this.selectedBookingDetail = this.bookings.find(booking => booking.bookingId === bookingId);
    }
  }

  goToPayment(bookingId: number){
    this.router.navigate(['/payment'],{ state: { bookingId: bookingId } })
  }
}
