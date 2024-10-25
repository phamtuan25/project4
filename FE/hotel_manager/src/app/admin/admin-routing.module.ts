import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin.component';
import { RoomManagerComponent } from './room-manager/room-manager.component';
import { BookingManagerComponent } from './booking-manager/booking-manager.component';
import { ServiceManagerComponent } from './service-manager/service-manager.component';
import { UserManagerComponent } from './user-manager/user-manager.component';

const routes: Routes = [
    {
        path: '',
        component: AdminComponent,
        children: [
            { path: 'room-manager', component: RoomManagerComponent },
            { path: 'booking-manager', component: BookingManagerComponent },
            { path: 'service-manager', component: ServiceManagerComponent },
            { path: 'user-manager', component: UserManagerComponent },
        ]
    }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }