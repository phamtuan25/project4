import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import $ from 'jquery';
import 'bootstrap';
import { AdminService } from '../admin.service';
import { Booking } from '../booking-manager/booking-manager.component';
import { NgForm } from '@angular/forms';
import Modal from 'bootstrap/js/dist/modal';
@Component({
  selector: 'app-user-manager',
  templateUrl: './user-manager.component.html',
  styleUrl: './user-manager.component.css'
})


export class UserManagerComponent implements OnInit {
  users: User[] = [];
  userId: number = 0;
  firstName: string = "";
  lastName: string = "";
  email: string = "";
  address: string = "";
  phoneNumber: string = ""; 
  role: string = "EMPLOYEE";
  password: string = "";
  fullName: string = "";
  totalUsers: number = 0;
  pageSize: number = 10;
  currentPage: number = 1;
  totalPages: number = 0;
  keyword: string = '';
  editModal!: Modal;
  createModal!: Modal;
  constructor(public admin: AdminComponent,private adminService: AdminService){}
  
  ngOnInit(): void {
    this.admin.pageTitle = 'User Management';
    this.getUsers(this.currentPage , this.pageSize, this.keyword);
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

  getUsers(page: number, size: number, keyword: string){
    this.adminService.getUser(page - 1, size, keyword).subscribe(
      (response: any) => {
        this.users = response.content;
        this.totalUsers = response.totalElements;
        this.totalPages = Math.ceil(this.totalUsers / this.pageSize);
      },
      (error) => {
        console.error("Error fetching users", error);
        this.users = [];
        this.errors.push(error);
      }
    )
  }
  goToPage(page: number): void {
    if (page < 1 || page > Math.ceil(this.totalUsers / this.pageSize)) return;
    this.currentPage = page;
    this.getUsers(this.currentPage, this.pageSize, this.keyword);
  }
  searchUsers(): void {
    const input: string = (document.getElementById('searchUserInput') as HTMLInputElement).value.trim();
    this.keyword = input;
    this.currentPage = 1;
    this.getUsers(this.currentPage, this.pageSize, this.keyword);
  }
  handleKeyPress(event: any) {
    if (event.key === 'Enter') {
      this.searchUsers();
    }
  }
  openAddUser() {
    this.resetFormData();
    this.createModal.show();
  }
  openEditUser(user: User) {
    this.userId = user.userId
    this.email = user.email
    this.fullName = user.fullName
    this.address = user.address
    this.phoneNumber = user.phoneNumber
    this.role = user.role
    this.editModal.show();
  }
  onSubmitEdit(form: NgForm) {
    if(form.valid) {
      this.adminService.editUser(this.userId, this.address, this.email, this.phoneNumber,this.role).subscribe(
        response => {
          alert("Edit Success!");
          this.getUsers(this.currentPage , this.pageSize, this.keyword);
          this.resetFormData();
        },
      );
    }
    
  }

  errors:any=[];
  onSubmitAdd() {
    this.adminService.addUser(this.firstName, this.lastName, this.address, this.email, this.phoneNumber, this.password, this.role).subscribe(
      response => {
        alert("Add Success!");
        this.getUsers(this.currentPage , this.pageSize, this.keyword);
        this.resetFormData();
      },
      error => {
        this.errors = [];
        if(this.isObject(error.error)){
          this.errors.push(error.error)
        }
        error.error.forEach((element:any) => {
          this.errors.push(element);
        });
        console.log("error",error.error)
      }
    );
  }
  findErrors(key:string){
    return this.errors.find((error:any)=>error.key==key)?.message;
  }
  resetFormData() {
    this.firstName = "";
    this.lastName = "";
    this.email = "";
    this.address = "";
    this.phoneNumber = "";
    this.password = "";
    this.editModal.hide();
    this.createModal.hide();
  }
  isObject(value: any) {
    return typeof value === 'object' && value !== null && !Array.isArray(value);
  }
}

export interface User {
  userId: number;
  fullName: string; 
  email: string;
  address: string;
  phoneNumber: string; 
  role: string;
  bookings: [];
}






