import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookingManagerComponent } from './booking-manager/booking-manager.component';
import { RoomManagerComponent } from './room-manager/room-manager.component';
import { UserManagerComponent } from './user-manager/user-manager.component';
import { AdminRoutingModule } from './admin-routing.module';
import { AdminComponent } from './admin.component';
import { FormsModule } from '@angular/forms';
import { ProvisionManagerComponent } from './provision-manager/provision-manager.component';
import { BookingDetailManagerComponent } from './booking-detail-manager/booking-detail-manager.component';
import { ContactManagerComponent } from './contact-manager/contact-manager.component';

@NgModule({
  declarations: [
    AdminComponent,
    BookingManagerComponent,
    RoomManagerComponent,
    ProvisionManagerComponent,
    UserManagerComponent,
    BookingDetailManagerComponent,
    ContactManagerComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    FormsModule
  ]
})
export class AdminModule { }
