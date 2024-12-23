import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import $ from 'jquery';
import 'bootstrap';
import { AdminService, Images } from '../admin.service';
import { NgForm } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ChangeDetectorRef } from '@angular/core';
import Modal from 'bootstrap/js/dist/modal';
@Component({
  selector: 'app-room-manager',
  templateUrl: './room-manager.component.html',
  styleUrl: './room-manager.component.css'
})
export class RoomManagerComponent implements OnInit {
  rooms: Room[] = [];
  keyword: string = '';
  roomId: number = 0;
  roomNumber: string = "";
  roomType: string = "SINGLE";
  status: string = "AVAILABLE";
  description: string = "";
  dayPrice: number = 0;
  hourPrice: number = 0;
  files: File[] | null = [];
  errors: any[] = [];
  imagePaths: ImagePaths[] = [];
  deleteFiles: string[] = [];
  imageOrigin: string[] = [];
  totalRooms: number = 0;
  pageSize: number = 10;
  currentPage: number = 1;
  totalPages: number = 0;
  selectedRoom: any;
  editModal!: Modal;
  createModal!: Modal;
  constructor(public admin: AdminComponent, private adminService: AdminService, private http: HttpClient, private cdr: ChangeDetectorRef) { }
  ngOnInit(): void {
    this.admin.pageTitle = 'Room Management';
    this.getRooms(this.currentPage , this.pageSize, this.keyword);
  }

  ngAfterViewInit() {
    this.editModal = new Modal('#editModal', {
      keyboard: false,
      backdrop: 'static'
    });
    this.createModal = new Modal('#createModal', {
      keyboard: false,
      backdrop: 'static'
    });
  }

  //get list Room
  getRooms(page: number, size: number, keyword: string) {
    this.adminService.getRooms(page - 1, size, keyword).subscribe(
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
    if (page < 1 || page > Math.ceil(this.totalRooms / this.pageSize)) return;
    this.currentPage = page;
    this.getRooms(this.currentPage, this.pageSize, this.keyword);
  }


  // tìm kiếm Room
  searchRooms(): void {
    const input: string = (document.getElementById('searchRoomInput') as HTMLInputElement).value.trim();
    this.keyword = input;
    this.currentPage = 1;
    this.getRooms(this.currentPage, this.pageSize, this.keyword);
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
      console.log('this.files', this.files)
      Array.from(input.files).forEach(file => {
        const reader = new FileReader();
        reader.onload = (e) => {
          if (e.target && e.target.result) {
            this.imagePaths.push({
              name: file.lastModified + file.name,
              path: e.target.result as string
            });
            this.cdr.detectChanges();
          }
        };
        reader.readAsDataURL(file);
      });
      console.log("this.imagePaths", this.imagePaths)
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
    this.adminService.addRoom(this.roomNumber, this.roomType,this.description, this.status, this.dayPrice, this.hourPrice, this.files).subscribe(
      response => {
        alert("Add Success!");
        this.files = [];
        this.resetFormData();
        this.getRooms(this.currentPage, this.pageSize, this.keyword);
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

  openAddRoom() {
    this.resetFormData();
    this.createModal.show();
  }

  // gán giá trị Room edit
  openEditRoom(room: Room) {
    this.selectedRoom = room
    this.roomId = room.roomId
    this.roomNumber = room.roomNumber
    this.description = room.description
    this.roomType = room.roomType
    this.status = room.status
    this.dayPrice = room.dayPrice
    this.hourPrice = room.hourPrice
    this.imageOrigin = room.images
    room.images.map(image => {
      this.convertImageToBase64('http://localhost:8080/upload_images/' + image).subscribe(base64 => {
        this.imagePaths.push({
          name: image,
          path: base64
        });
      }, error => {
        console.error('Error converting image:', error);
      });
    })
    this.editModal.show();
  }
  // submit room đã edit
  onSubmitEdit(form: NgForm) {
    console.log(form.valid)
    if (form.valid) {
      this.adminService.eidtRoom(this.roomId, this.roomNumber, this.roomType,this.description, this.status, this.dayPrice, this.hourPrice, this.files, this.deleteFiles).subscribe(
        response => {
          alert("Edit Success!");
          this.getRooms(this.currentPage, this.pageSize, this.keyword);
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
    this.description = "";
    this.dayPrice = 0;
    this.hourPrice = 0;
    this.files = null;
    this.errors = [];
    this.imagePaths = [];
    this.createModal.hide();
    this.editModal.hide();
  }
  removeImage(image: ImagePaths, index: number) {
    this.imagePaths.splice(index, 1);
    this.files = this.files?.filter(file => file.lastModified + file.name !== image.name) ?? null;
    this.deleteFiles = this.imageOrigin.filter(ori =>
      !this.imagePaths.some(path => path.name === ori)
    );
  }
  convertImageToBase64(imageUrl: string): Observable<string> {
    return new Observable(observer => {
      this.http.get(imageUrl, { responseType: 'blob' }).subscribe(blob => {
        const reader = new FileReader();
        reader.onloadend = () => {
          observer.next(reader.result as string);
          observer.complete();
        };
        reader.readAsDataURL(blob);
      }, error => {
        observer.error(error);
      });
    });
  }
}

export interface Room {
  roomId: number;
  roomNumber: string;
  roomType: string;
  status: string;
  description: string;
  dayPrice: number;
  hourPrice: number;
  images: string[];
}

interface ImagePaths {
  name: string,
  path: string;
}

