import { Component, OnInit } from '@angular/core';
import { ClientComponent } from '../client.component';
import { ClientService } from '../client.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Room } from '../room/room.component';
declare var $: any
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  checkin: string = '';
  checkout: string = '';
  roomType: string = '';
  roomTypes: string[] = ['SINGLE', 'DOUBLE', 'FAMILY', 'DELUXE'];
  rooms: Room[] = [];
  errors: any[] = [];
  totalRooms: number = 0;

  constructor(public client: ClientComponent, private clientService: ClientService, private router: Router) { }
  ngOnInit(): void {
    this.client.pageTitle = 'Home Page';

    const roomType = 'FAMILY';
    const pageSize = 4;

    this.getRoomsHome(pageSize, roomType);
  }


  goToDetail(roomId: number): void {
    this.router.navigate(['/room-detail', roomId], {
      state: { fromPage: this.router.url }
    });
  }
  getRoomsHome(size: number, roomType: string): void {
    this.clientService.getRoomsHome(0, size, '', 'AVAILABLE', roomType).subscribe(
      (response: any) => {
        console.log('API response:', response);

        if (Array.isArray(response.content)) {
          console.log('Response is an array');
        } else {
          console.error('Response is not an array!');
        }
        this.rooms = response.content.filter((room: any) => room.roomType.toUpperCase() === roomType.toUpperCase());
        console.log('Filtered rooms:', this.rooms);
        if (this.rooms.length === 0) {
          console.log('There are no FAMILY rooms available.');
        }
      },
      (error) => {
        console.error("Error fetching rooms", error);
        this.rooms = [];
        this.errors.push(error);
      }
    );
  }

  checkRoomAvailability(): void {
    if (this.checkin && this.checkout && this.roomType) {
      const params = {
        checkin: this.checkin,
        checkout: this.checkout,
        roomType: this. roomType
      }
      this.router.navigate(['/room-availability'], { state: {params: params} });
    } else {
      this.errors.push('Please fill in all fields.');
    }
  }
}

