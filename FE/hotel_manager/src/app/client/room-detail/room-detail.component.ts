import { Component, OnInit, AfterContentInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms'; 
import { ClientService } from '../client.service';
import { Room } from '../room/room.component';
import { ActivatedRoute, Router } from '@angular/router';

declare var $: any;

@Component({
  selector: 'app-room-detail',
  templateUrl: './room-detail.component.html',
  styleUrls: ['./room-detail.component.css']
})
export class RoomDetailComponent implements OnInit, AfterContentInit {
  roomDetail: Room | null = null;
  errors: any[] = [];
  roomId: number = 0;
  currentPage: number = 1;
  pricingOption: string = 'daily'; 
  bookingForm: FormGroup;

  constructor(
    private fb: FormBuilder, 
    private clientService: ClientService,
    private route: ActivatedRoute,
    private router: Router,
  ) {
   
    this.bookingForm = this.fb.group({
      checkin: ['', Validators.required],
      checkout: ['', Validators.required],
      pricingOption: ['daily', Validators.required], 
      specialRequests: ['', Validators.required],
      price: [{ value: '', disabled: true }, Validators.required]  
    });
  }

  ngOnInit(): void {
    this.roomId = +this.route.snapshot.paramMap.get('roomId')!;
    this.getRoomDetail(this.roomId);

    this.route.queryParams.subscribe(params => {
      const page = params['page'];
      if (page) {
        this.currentPage = +page;
      }
    });

    this.updatePrice();
  }

  ngAfterContentInit(): void {
    const myCarousel = document.querySelector('#roomCarousel');
    if (myCarousel) {
      $(myCarousel).carousel(); 
    }
  }

  getRoomDetail(roomId: number): void {
    this.clientService.getRoomDetail(roomId).subscribe(
      (response: Room) => {
        this.roomDetail = response;
        this.updatePrice(); 
      },
      (error) => {
        console.error("Error fetching room details", error);
        this.errors.push(error);
      }
    );
  }

  updatePrice(): void {
    if (this.roomDetail) {
      const price = this.pricingOption === 'hourly' ? this.roomDetail.hourPrice : this.roomDetail.dayPrice;
      this.bookingForm.patchValue({ price }); 
    }
  }

  onPricingOptionChange(event: any): void {
    this.pricingOption = event.target.value;
    this.bookingForm.patchValue({ pricingOption: this.pricingOption });
    this.updatePrice();
  }

  goBack(): void {
    this.router.navigate(['/room'], {
      queryParams: { page: this.currentPage }
    });
  }

  addRoomToBooking(): void {
    if (this.bookingForm.valid) {
      console.log('Booking form submitted with:', this.bookingForm.value);
    } else {
      this.errors.push('Please fill in both check-in and check-out fields.');
    }
  }
}
