import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin.component';
import { RoomManagerComponent } from './room-manager/room-manager.component';
import { BookingManagerComponent } from './booking-manager/booking-manager.component';
import { ProvisionManagerComponent } from './provision-manager/provision-manager.component';
import { UserManagerComponent } from './user-manager/user-manager.component';
import { BookingDetailManagerComponent } from './booking-detail-manager/booking-detail-manager.component';

const routes: Routes = [
    {
        path: '',
        component: AdminComponent,
        children: [
            { path: 'room-manager', component: RoomManagerComponent },
            { path: 'booking-manager', component: BookingManagerComponent },
            { path: 'provision-manager', component: ProvisionManagerComponent },
            { path: 'user-manager', component: UserManagerComponent },
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