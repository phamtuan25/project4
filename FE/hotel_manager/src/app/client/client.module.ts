import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { BookingComponent } from './booking/booking.component';
import { ProvisionComponent } from './provision/provision.component';
import { RoomComponent } from './room/room.component';
import { RoomDetailComponent } from './room-detail/room-detail.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { UserBookingComponent } from './user-booking/user-booking.component';
import { ClientRoutingModule } from './client-routing.module';
import { ClientComponent } from './client.component';
import { ContactComponent } from './contact/contact.component';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';  

@NgModule({
  declarations: [
    ClientComponent,
    HomeComponent,
    BookingComponent,
    ProvisionComponent,
    RoomComponent,
    RoomDetailComponent,
    UserProfileComponent,
    UserBookingComponent,
    ContactComponent
  ],
  imports: [
    CommonModule,
    ClientRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    
  ]
})
export class ClientModule { }
