import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ConfigService } from '../../../config/config.service';
import { ClientService } from '../client.service';
import { ClientComponent } from '../client.component';
import { Booking } from '../../admin/booking-manager/booking-manager.component';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css'
})
export class PaymentComponent implements OnInit {
  bookingDetail: Booking | null = null;
  errors: any[] = [];
  pricingOption: string = 'PENDING';  
  paymentReference: string = '';
  bookingForm: FormGroup;
  bookingId: number | null = null;
  
  constructor(private router: Router, private config: ConfigService, private clientService: ClientService, private client: ClientComponent, private fb: FormBuilder) {
    this.bookingForm = this.fb.group({
      pricingOption: ['PENDING', Validators.required],
      price: [{ value: '', disabled: true }],
      paymentReference: [{ value: '', disabled: true }],
      bookingId: ['']
    });
   }

  ngOnInit() {
    if (history.state.bookingId) {
      const bookingId = history.state.bookingId || [];
      this.getBookingDetailById(bookingId);
      this.bookingId = bookingId;
    }
  }


  getBookingDetailById(bookingId: number): void {
    this.clientService.getBookingByBookingId(bookingId).subscribe(
      (response: Booking) => {
        this.bookingDetail = response;
      },
      (error) => {
        console.error("Error fetching room details", error);
        this.errors.push(error);
      }
    );
  }
  updatePaymentAmount(): void {
    if (this.bookingDetail) {
      const price = this.pricingOption === 'PAID' ? this.bookingDetail.totalAmount : this.bookingDetail.deposit;
      this.bookingForm.patchValue({ price });

    }
  }
  updatePaymentReference(): void {
    if (this.bookingDetail) {
      const user = this.bookingDetail.userBookingResponse;  
      if (user) {
        const paymentReference = user.fullName + (this.pricingOption === 'DEPOSITED' ? ' DEPOSITED' : ' PAID');
        this.bookingForm.patchValue({ paymentReference });  
      }
    }
  }
  onPricingOptionChange(event: any): void {
    this.pricingOption = event.target.value;  
    this.bookingForm.patchValue({ pricingOption: this.pricingOption });  
    this.updatePaymentAmount();  
    this.updatePaymentReference();
  }

  submitPayment(): void {
    const paymentRequest = {
      bookingId: this.bookingId,
      paymentMethod: this.pricingOption,
      price: this.bookingForm.get('price')?.value
    };

    this.clientService.addPayment(paymentRequest).subscribe(
      (response) => {
        alert('Payment successful');
        this.router.navigate(['/payment-info']);
      },
      (error) => {
        alert(error?.error?.message || 'Payment failed');
      }
    );
  }
}
