import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClientComponent } from './client.component';
import { HomeComponent } from './home/home.component';
import { RoomComponent } from './room/room.component';
import { BookingComponent } from './booking/booking.component';
import { ProvisionComponent } from './provision/provision.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { RoomDetailComponent } from './room-detail/room-detail.component';
import { ContactComponent } from './contact/contact.component';

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
              { path: 'booking', component: BookingComponent },
              { path: 'provision', component: ProvisionComponent },
              { path: 'user-profile', component: UserProfileComponent },
              { path: 'contact', component: ContactComponent },
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