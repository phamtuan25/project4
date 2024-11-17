import { Component, OnInit } from '@angular/core';
import { ClientComponent } from '../client.component';
import { ClientService } from '../client.service';
import { ActivatedRoute, Router } from '@angular/router'; 
@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.css']
})
export class RoomComponent implements OnInit {
  rooms: Room[] = [];
  keyword: string = '';
  errors: any[] = []; 
  totalRooms: number = 0;
  pageSize: number = 6; 
  currentPage: number = 1;
  totalPages: number = 0;

  constructor(
    public client: ClientComponent, 
    private clientService: ClientService, 
    private router: Router,        
    private route: ActivatedRoute  
  ) { }

  ngOnInit(): void {
    this.client.pageTitle = 'Room Page';

    this.route.queryParams.subscribe(params => {
      const page = params['page'];
      if (page) {
        this.currentPage = +page;
      }
      this.getRooms(this.currentPage, this.pageSize, this.keyword); 
    });
  }

  goToDetail(roomId: number): void {
    this.router.navigate(['/room-detail', roomId], {
      state: { fromPage: 'room' ,page: this.currentPage } 
    });
  }
  getRooms(page: number, size: number, keyword: string): void {
    this.clientService.getRooms(page - 1, size, keyword, 'AVAILABLE').subscribe(
      (response: any) => {
        this.rooms = response.content;
        this.totalRooms = response.totalElements;
        this.totalPages = Math.ceil(this.totalRooms / this.pageSize);
      },
      (error) => {
        console.error("Error fetching rooms", error);
        this.rooms = [];
        this.errors.push(error);
      }
    );
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;

    this.router.navigate([], {
      queryParams: { page: this.currentPage },
      queryParamsHandling: 'merge',
    });
  }
}

export interface Room {
  roomId: number;
  roomNumber: string;
  roomType: string;
  description: string;
  status: string;
  dayPrice: number;
  hourPrice: number;
  images: string[];
}
