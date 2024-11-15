import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClientComponent } from './client.component';
import { HomeComponent } from './home/home.component';
import { RoomComponent } from './room/room.component';
import { ProvisionComponent } from './provision/provision.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { RoomDetailComponent } from './room-detail/room-detail.component';
import { ContactComponent } from './contact/contact.component';
import { UserBookingComponent } from './user-booking/user-booking.component';
import { IntroduceComponent } from './introduce/introduce.component';
import { PaymentComponent } from './payment/payment.component';
import { RoomAvailabilityComponent } from './room-availability/room-availability.component';

const routes: Routes = [
    {
        path: '',
        component: ClientComponent,
        children: [
          {
            path: '',
            component: HomeComponent,
          },
          { path: 'room', component: RoomComponent },
              { path: 'provision', component: ProvisionComponent },
              { path: 'user-profile', component: UserProfileComponent },
              { path: 'user-booking', component: UserBookingComponent },
              { path: 'contact', component: ContactComponent },
              { path: 'introduce', component: IntroduceComponent },
              { path: 'payment', component: PaymentComponent },
              { path: 'room-availability', component: RoomAvailabilityComponent },
              { path: 'room-detail/:roomId', component: RoomDetailComponent},
              { path: '', redirectTo: '/room', pathMatch: 'full' },
        ]
    }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientRoutingModule { }