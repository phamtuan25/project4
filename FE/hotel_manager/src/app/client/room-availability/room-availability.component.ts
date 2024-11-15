import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ClientService } from '../client.service';

// Định nghĩa interface cho state, cụ thể là các phòng
@Component({
  selector: 'app-room-availability',
  templateUrl: './room-availability.component.html',
  styleUrls: ['./room-availability.component.css']
})
export class RoomAvailabilityComponent implements OnInit {
  rooms: Room[] = [];
  constructor(private router: Router, private clientService: ClientService) { }

  ngOnInit(): void {
    if (history.state.params) {
      const params = history.state.params || [];
      this.getRooms(params);
    }
  }

  goToDetail(roomId: number): void {
    this.router.navigate(['/room-detail', roomId], {
      state: { fromPage: 'room-availability', params: history.state.params}
    });
  }
  getRooms(params: any) {
    this.clientService.getAvailableRooms(params.checkin, params.checkout, params.roomType).subscribe(
      (response: any) => {
        this.rooms = response; 
      },
      (error) => {
        console.error('Error fetching available rooms', error);
        this.rooms = [];
      }
    );
  }
}

export interface Room {
  roomId: number;
  roomNumber: string;
  roomType: string;
  status: string;
  dayPrice: number;
  hourPrice: number;
  images: string[];
}
