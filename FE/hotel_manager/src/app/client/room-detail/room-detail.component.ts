import { Component, OnInit, AfterContentInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { ClientService } from '../client.service';
import { Room } from '../room/room.component';
import { ActivatedRoute, Router } from '@angular/router';
import { ConfigService } from '../../../config/config.service';
import { User } from '../../admin/user-manager/user-manager.component';
import { GlobalStateService } from '../../../config/global.stage.service';
import { Subscription } from 'rxjs';

declare var $: any;

@Component({
  selector: 'app-room-detail',
  templateUrl: './room-detail.component.html',
  styleUrls: ['./room-detail.component.css']
})
export class RoomDetailComponent implements OnInit, AfterContentInit, OnDestroy {
  roomDetail: Room | null = null;
  errors: any[] = [];
  roomId: number = 0;
  currentPage: number = 1; 
  pricingOption: string = 'daily';
  bookingForm: FormGroup;
  userLogin: User | null = null;
  availableProvisions: { provisionId: number, provisionName: string, price: number }[] = [];
  selectedProvision: string[] = [];
  private userSubscription!: Subscription;

  constructor(
    private fb: FormBuilder,
    private clientService: ClientService,
    private route: ActivatedRoute,
    private router: Router,
    private config: ConfigService,
    private globalStageService: GlobalStateService
  ) {
    this.bookingForm = this.fb.group({
      checkIn: ['', Validators.required],
      checkOut: ['', Validators.required],
      pricingOption: ['daily', Validators.required],
      specialRequests: ['', Validators.required],
      price: [{ value: '', disabled: true }, Validators.required],
      provisions: this.fb.array([]),
    });
  }

  ngOnInit(): void {
    this.loadAvailableProvisons();
    this.roomId = +this.route.snapshot.paramMap.get('roomId')!;
    this.getRoomDetail(this.roomId);

    this.userSubscription = this.globalStageService.getUserStage().subscribe(user => {
      this.userLogin = user;
    });
    this.route.queryParams.subscribe(params => {
      const page = params['page'];
      if (page) {
        this.currentPage = +page; 
      }
    });

    this.updatePrice();
  }

  ngOnDestroy() {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
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
    const previousPage = history.state.fromPage || '/';
    const previousPageNum = history.state.page || 1;
    if (previousPage === 'room-availability') {
      this.router.navigate(['/room-availability'], {
        queryParams: { page: previousPageNum},
        state: {params: history.state.params}
      });
    } else if (previousPage === 'room') {
      this.router.navigate(['/room'], {
        queryParams: { page: previousPageNum }
      });
    } else {
      this.router.navigate(['/']);
    }
  }
  

  get provisions(): FormArray {
    return this.bookingForm.get('provisions') as FormArray;
  }

  loadAvailableProvisons(): void {
    this.clientService.getProvisionBooking().subscribe(
      (response: any) => {
        this.availableProvisions = response.content;
      },
      (error) => {
        console.error('Error fetching services', error);
      }
    );
  }

  onProvisionChange(provisionName: string, event: any): void {
    if (event.target.checked) {
      if (!this.selectedProvision.includes(provisionName)) {
        this.selectedProvision.push(provisionName);
      }
    } else {
      const index = this.selectedProvision.indexOf(provisionName);
      if (index !== -1) {
        this.selectedProvision.splice(index, 1);
      }
    }
    this.updateFormArray();
  }

  updateFormArray(): void {
    this.provisions.clear();

    this.selectedProvision.forEach((provisionName: string) => {
      if (!this.isProvisionAlreadyAdded(provisionName)) {
        this.provisions.push(this.fb.control(provisionName));
      }
    });
  }

  isProvisionAlreadyAdded(provisionName: string): boolean {
    return this.provisions.controls.some(control => control.value === provisionName);
  }

  removeProvision(index: number): void {
    this.provisions.removeAt(index);
  }


  addToBooking(): void {
    if (this.bookingForm.valid) {
      if (!this.userLogin) {
        alert('You need to log in to your account to book');
        this.router.navigate(['/login']);
        return;
      }
  
      const roomId = this.roomDetail?.roomId || this.bookingForm.get('roomId')?.value;
      const formValues = this.bookingForm.value;
      const { pricingOption, provisions, ...formWithoutRoomType } = formValues;
  
      const provisionIds = this.selectedProvision.map(provisionName => {
        const provision = this.availableProvisions.find(p => p.provisionName === provisionName);
        return provision ? provision.provisionId : null; 
      }).filter(id => id !== null);
  
      const requestData = {
        bookingDetailRequests: [{
          roomId,
          provisionIds,
          checkIn: formWithoutRoomType.checkIn,
          checkOut: formWithoutRoomType.checkOut,
          specialRequests: formWithoutRoomType.specialRequests
        }],
        user: { userId: this.userLogin?.userId }
      };
  
      console.log('Request Data:', requestData);  
  
      this.clientService.addBooking(this.userLogin?.userId, requestData).subscribe(
        (response: any) => {
          console.log('API Response:', response);
          alert('Room booked successfully');
          this.router.navigate(['/booking-success']);
        },
        (error) => {
          console.error('API Error:', error);  
          alert(error?.error?.message || 'An error occurred while booking the room.');
        }
      );
    } else {
      this.errors.push('Please fill in both check-in and check-out fields.');
    }
  }
  
  
  
}
