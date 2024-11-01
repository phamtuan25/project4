import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import $ from 'jquery';
import 'bootstrap';
import { AdminService, Images } from '../admin.service';
import { NgForm } from '@angular/forms';
@Component({
  selector: 'app-room-manager',
  templateUrl: './room-manager.component.html',
  styleUrl: './room-manager.component.css'
})
export class RoomManagerComponent implements OnInit {
  rooms: Room[] = [];
  filterRooms: Room[] = [];
  roomId: number = 0;
  roomNumber: string = "";
  roomType: string = "SINGLE";
  status: string = "AVAILABLE";
  dayPrice: number = 0;
  hourPrice: number = 0;
  files: File[] | null = [];
  errors: any[] = [];
  constructor(public admin: AdminComponent, private adminService: AdminService) { }
  ngOnInit(): void {
    this.admin.pageTitle = 'Room Management';
    this.getRooms();
  }

  //get list Room
  getRooms() {
    this.adminService.getRoom().subscribe(
      (response: Room[]) => {
        this.rooms = response;
        this.filterRooms = response
      }
    )
  }
  // tìm kiếm Room
  searchRooms(): void {
    const input: string = (document.getElementById('searchRoomInput') as HTMLInputElement).value.toLowerCase();
    this.filterRooms = this.rooms.filter(room => {
      return room.roomNumber?.toLowerCase().includes(input) ||
        room.roomType?.toLowerCase().includes(input) ||
        room.status?.toLowerCase().includes(input) ||
        String(room.dayPrice).toLowerCase().includes(input) ||
        String(room.hourPrice).toLowerCase().includes(input);
    });
  }
  handleKeyPress(event: any) {
    if (event.key === 'Enter') {
      this.searchRooms();
    }
  }


  onFileChange(event: Event) {
    const input = event.target as HTMLInputElement;
    
    this.errors = [];
    if (input.files && input.files.length > 0) {
        this.files = Array.from(input.files);

        // Kiểm tra định dạng tệp
        const validFormats = ['image/jpeg', 'image/png', 'image/gif'];
        if (!this.files.some(file => validFormats.includes(file.type))) {
            this.errors.push({ key: 'images', message: 'Only JPEG, PNG or GIF format images are accepted.' });
        }
    } else {
        this.files = [];
        this.errors.push({ key: 'images', message: 'Please select an image.' });
    }
}
  //submit room đã add
 

  onSubmitAdd() {
    this.adminService.addRoom(this.roomNumber, this.roomType, this.status, this.dayPrice, this.hourPrice, this.files).subscribe(
        response => {
            alert("Add Success!");
            const modalElement = document.getElementById(`addRoomModal`);
            if (modalElement) {
                modalElement.style.display = 'none'; 
                modalElement.classList.remove('show');
            }
            const backdrop = document.querySelector('.modal-backdrop.fade.show');
            if (backdrop) {
                document.body.removeChild(backdrop);
            }
            this.files=[];
            this.getRooms();
            this.resetFormData();
        },
        error => {
            this.errors = [];
            if (error.error && Array.isArray(error.error)) {
                error.error.forEach((element: any) => {
                    this.errors.push({ key: element.key || 'unknown', message: element.message || 'An error occurred' });
                });
            } else {
                this.errors.push({ key: 'general', message: 'An unknown error has occurred. Please try again!' });
            }
            console.log("error", error.error);
        }
    );
}

findErrors(key: string) {
    return this.errors.find((error: any) => error.key === key)?.message;
}



// gán giá trị Room edit
  openEditRoom(room: Room) {
    this.roomId = room.roomId
    this.roomNumber = room.roomNumber
    this.roomType = room.roomType
    this.status = room.status
    this.dayPrice = room.dayPrice
    this.hourPrice = room.hourPrice
  }
  // submit room đã edit
  onSubmitEdit(form: NgForm) {
    console.log(form.valid)
    if(form.valid) {
      this.adminService.eidtRoom(this.roomId, this.roomNumber, this.roomType, this.status,this.dayPrice, this.hourPrice).subscribe(
        response => {
          alert("Edit Success!");
          const modalElement = document.getElementById(`editRoomModal${this.roomId}`);
          if (modalElement) {
            modalElement.style.display = 'none'; 
            modalElement.classList.remove('show');
          }
          const backdrop = document.querySelector('.modal-backdrop.fade.show');
          if (backdrop) {
            document.body.removeChild(backdrop);
          }
          this.getRooms();
          this.resetFormData();
        },
      );
    }
  }
  // reset lại biến
  resetFormData() {
    this.roomNumber = "";
    this.roomType = "SINGLE";
    this.status = "AVAILABLE";
    this.dayPrice = 0;
    this.hourPrice = 0;
    this.files = null;
  }
}

export interface Room {
  roomId: number;
  roomNumber: string;
  roomType: string;
  status: string;
  dayPrice: number;
  hourPrice: number;
  images: Images[];
}


