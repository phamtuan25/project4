import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import { AdminService } from '../admin.service';
import { Router } from '@angular/router';
import Modal from 'bootstrap/js/dist/modal';
@Component({
  selector: 'app-provision-booking-manager',
  templateUrl: './provision-booking-manager.component.html',
  styleUrl: './provision-booking-manager.component.css'
})
export class ProvisionBookingManagerComponent implements OnInit{
  provisionBookings: ProvisionBooking[] = [];
  errors: any[] = [];
  totalProvisionBookings: number = 0;
  pageSize: number = 10;
  currentPage: number = 1;
  totalPages: number = 0;
  keyword: string = '';
  editModal!: Modal;

  constructor(public admin: AdminComponent, private adminService: AdminService, private router: Router) { }
  ngOnInit(): void {
    this.admin.pageTitle = 'Booking Management';
    this.getProvisionBookings(this.currentPage, this.pageSize, this.keyword);
  }

  ngAfterViewInit() {
    this.editModal = new Modal('#editModal', {
      keyboard: false,
      backdrop: 'static'
    });
  }
  getProvisionBookings(page: number, size: number, keyword: string) {
    this.adminService.getProvisionBooking(page - 1, size, keyword).subscribe(
      (response: any) => {
        this.provisionBookings = response.content;
        this.totalProvisionBookings = response.totalElements;
        this.totalPages = Math.ceil(this.totalProvisionBookings / this.pageSize);
      },
      (error) => {
        console.error("Error fetching provision booking", error);
        this.provisionBookings = [];
        this.errors.push(error);
      }
    );
  }
  goToPage(page: number): void {
    if (page < 1 || page > Math.ceil(this.totalProvisionBookings / this.pageSize)) return;
    this.currentPage = page;
    this.getProvisionBookings(this.currentPage, this.pageSize, this.keyword);
  }


  searchProvisionBookings(): void {
    const input: string = (document.getElementById('searchProvisionBookingsInput') as HTMLInputElement).value.trim();
    this.keyword = input;
    this.currentPage = 1;
    this.getProvisionBookings(this.currentPage, this.pageSize, this.keyword);
  }
  handleKeyPress(event: any) {
    if (event.key === 'Enter') {
      this.searchProvisionBookings();
    }
  }
}
export interface ProvisionBooking {
  relId: number;
  provisionId: number;
  bookingDetailId: number;
  roomNumber: string,
  provisionName: string,
  createdAt: Date; 
  updatedAt: Date; 
  status: string; 
  price: number;
}