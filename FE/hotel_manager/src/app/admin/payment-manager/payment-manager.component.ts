import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import { AdminService } from '../admin.service';

@Component({
  selector: 'app-payment-manager',
  templateUrl: './payment-manager.component.html',
  styleUrl: './payment-manager.component.css'
})
export class PaymentManagerComponent implements OnInit{
  payments: Payment[] = [];
  totalPayments: number = 0;
  pageSize: number = 10;
  currentPage: number = 1;
  totalPages: number = 0;
  keyword: string = '';
  constructor(public admin: AdminComponent,private adminService: AdminService){}
  ngOnInit(): void {
    this.admin.pageTitle = 'Payment Management';
    this.getPayments(this.currentPage , this.pageSize, this.keyword);
  }

  //Get Payment list
  getPayments(page: number, size: number, keyword: string) {
    this.adminService.getPayment(page - 1, size, keyword).subscribe(
      (response: any) => {
        this.payments = response.content; 
        this.totalPages = Math.ceil(this.totalPayments / this.pageSize);
        console.log('Fetched contacts:', this.payments); 
      },
      (error) => {
        console.error("Error fetching contacts", error);
        this.payments = [];  
      }
    );
  }
  goToPage(page: number): void {
    if (page < 1 || page > Math.ceil(this.totalPayments / this.pageSize)) return;
    this.currentPage = page;
    this.getPayments(this.currentPage, this.pageSize, this.keyword);
  }
  //Tìm kiếm Payment 
  searchPayments(): void {
    const input: string = (document.getElementById('searchPaymentsInput') as HTMLInputElement).value.trim();
    this.keyword = input;
    this.currentPage = 1;
    this.getPayments(this.currentPage, this.pageSize, this.keyword);
  }
  handleKeyPress(event: any) {
    if (event.key === 'Enter') {
      this.searchPayments();
    }
  }
}

export interface Payment {
  paymentId: number;
  bookingId: number
  paymentMethod: string
  status: string; 
}
