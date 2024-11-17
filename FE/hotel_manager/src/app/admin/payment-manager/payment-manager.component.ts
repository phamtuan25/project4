import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import { AdminService } from '../admin.service';
import { Modal } from 'bootstrap';
import { NgForm } from '@angular/forms';

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
  paymentId: number = 0;
  user: string = '';
  paymentDate: string = '';
  paid: number = 0;
  paymentReference: string = '';
  status: string = 'PENDING';
  editModal!: Modal;
  constructor(public admin: AdminComponent,private adminService: AdminService){}
  ngOnInit(): void {
    this.admin.pageTitle = 'Payment Management';
    this.getPayments(this.currentPage , this.pageSize, this.keyword);
  }
  ngAfterViewInit() {
    this.editModal = new Modal('#editModal', {
      keyboard: false,
      backdrop: 'static'
    });
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

  openEditPayment(payment: Payment) {
    this.paymentId = payment.paymentId
    this.user = payment.user
    this.paymentReference = payment.paymentReference
    this.paymentDate = payment.paymentDate
    this.paid = payment.paid
    this.status = payment.status
    this.editModal.show();
  }

  onSubmitEdit(form: NgForm) {
    if (form.valid) {
      this.adminService.editPayment(this.paymentId, this.status).subscribe(
        response => {
          alert("Edit Success!");
          this.getPayments(this.currentPage , this.pageSize, this.keyword);
          this.resetFormData();
        },
      );
    }
  }
  resetFormData() {
    this.status = "";
    this.user = "";
    this.paymentDate = "";
    this.paymentReference = "";
    this.paid = 0;
    this.editModal.hide();
  }

  isObject(value: any) {
    return typeof value === 'object' && value !== null && !Array.isArray(value);
  }
}

export interface Payment {
  paymentId: number;
  user: string
  paymentDate: string
  paid: number
  paymentReference: string
  status: string; 
}
