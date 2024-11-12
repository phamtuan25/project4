import { Component, OnInit, AfterContentInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms'; 
import { ClientService } from '../client.service';
import { Room } from '../room/room.component';
import { ActivatedRoute, Router } from '@angular/router';
import { ConfigService } from '../../../config/config.service';
import { User } from '../../admin/user-manager/user-manager.component';

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
  userLogin: User | null = null

  constructor(
    private fb: FormBuilder, 
    private clientService: ClientService,
    private route: ActivatedRoute,
    private router: Router,
    private config: ConfigService
  ) {
   
    this.bookingForm = this.fb.group({
      checkIn: ['', Validators.required],
      checkOut: ['', Validators.required],
      pricingOption: ['daily', Validators.required], 
      specialRequests: ['', Validators.required],
      price: [{ value: '', disabled: true }, Validators.required]  
    });
  }

  ngOnInit(): void {
    this.roomId = +this.route.snapshot.paramMap.get('roomId')!;
    this.getRoomDetail(this.roomId);
    const email = this.config.getEmail();
    this.getUserLogin(email);

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

  addToBooking(): void {
    console.log('aaa',this.bookingForm.valid)
    if (this.bookingForm.valid) {
      const roomId = this.roomId;
      const formValues = this.bookingForm.value;

      const { pricingOption, ...formWithoutRoomType } = formValues;

      const requestData = { ...formWithoutRoomType, roomId };
      this.clientService.addBooking(this.userLogin?.userId, requestData).subscribe(
        (response: any) => {
          alert('Add room success')
        },
        (error) => {
          alert(error.error.message)
        }
      )

    } else {
      this.errors.push('Please fill in both check-in and check-out fields.');
    }
  }
  getUserLogin(email: string | null){
    this.clientService.getUserLogin(email).subscribe(
      (response: any) => {
        this.userLogin = response;
      },
      (error) => {
        console.error("Error fetching users", error);
      }
    )
  }
}


