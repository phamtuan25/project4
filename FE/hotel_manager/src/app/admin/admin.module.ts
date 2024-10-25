import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookingManagerComponent } from './booking-manager/booking-manager.component';
import { RoomManagerComponent } from './room-manager/room-manager.component';
import { ServiceManagerComponent } from './service-manager/service-manager.component';
import { UserManagerComponent } from './user-manager/user-manager.component';
import { AdminRoutingModule } from './admin-routing.module';
import { AdminComponent } from './admin.component';


@NgModule({
  declarations: [
    AdminComponent,
    BookingManagerComponent,
    RoomManagerComponent,
    ServiceManagerComponent,
    UserManagerComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule
  ]
})
export class AdminModule { }
