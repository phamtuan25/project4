import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin.component';
import { RoomManagerComponent } from './room-manager/room-manager.component';
import { BookingManagerComponent } from './booking-manager/booking-manager.component';
import { ProvisionManagerComponent } from './provision-manager/provision-manager.component';
import { UserManagerComponent } from './user-manager/user-manager.component';
import { BookingDetailManagerComponent } from './booking-detail-manager/booking-detail-manager.component';
import { ContactManagerComponent } from './contact-manager/contact-manager.component';
import { PaymentManagerComponent } from './payment-manager/payment-manager.component';
import { ProvisionBookingManagerComponent } from './provision-booking-manager/provision-booking-manager.component';

const routes: Routes = [
    {
        path: '',
        component: AdminComponent,
        children: [
            { path: 'room-manager', component: RoomManagerComponent },
            { path: 'booking-manager', component: BookingManagerComponent },
            { path: 'provision-manager', component: ProvisionManagerComponent },
            { path: 'user-manager', component: UserManagerComponent },
            { path: 'contact-manager', component: ContactManagerComponent },
            { path: 'payment-manager', component: PaymentManagerComponent },
            { path: 'provision-booking-manager', component: ProvisionBookingManagerComponent },
            { path: 'booking-detail-manager/:bookingId', component: BookingDetailManagerComponent },
            { path: '', redirectTo: '/booking-manager', pathMatch: 'full' }
        ]
    }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }