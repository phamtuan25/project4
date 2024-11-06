import { AfterContentInit, Component, OnInit } from '@angular/core';
import { ClientComponent } from '../client.component';
import { ActivatedRoute, Router } from '@angular/router'; 
import { ClientService } from '../client.service';
import { Room } from '../room/room.component';
declare var $: any

@Component({
  selector: 'app-room-detail',
  templateUrl: './room-detail.component.html',
  styleUrls: ['./room-detail.component.css'] 
})
export class RoomDetailComponent implements OnInit, AfterContentInit {
  roomDetail: Room | null = null;
  errors: any[] = [];
  roomId: number = 0;
  currentPage: number = 1; 
  constructor(
    public client: ClientComponent,
    private clientService: ClientService,
    private route: ActivatedRoute,
    private router: Router 
  ) {}
  ngAfterContentInit(): void {
    const myCarousel = document.querySelector('#roomCarousel');
    const carousel = $(myCarousel).carousel();
  }

  ngOnInit(): void {
    this.roomId = +this.route.snapshot.paramMap.get('roomId')!;
    this.getRoomDetail(this.roomId); 

    this.route.queryParams.subscribe(params => {
      const page = params['page'];
      if (page) {
        this.currentPage = +page; 
      }
    });
  }

  getRoomDetail(roomId: number): void {
    this.clientService.getRoomDetail(roomId).subscribe(
      (response: Room) => {
        this.roomDetail = response;
      },
      (error) => {
        console.error("Error fetching room details", error);
        this.errors.push(error);
      }
    );
  }

  goBack(): void {
    this.router.navigate(['/room'], {
      queryParams: { page: this.currentPage } 
    });
  }
}
