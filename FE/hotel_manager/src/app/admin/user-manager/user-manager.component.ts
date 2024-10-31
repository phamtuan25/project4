import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import $ from 'jquery';
import 'bootstrap';
import { AdminService } from '../admin.service';
import { Booking } from '../booking-manager/booking-manager.component';
import { NgForm } from '@angular/forms';
@Component({
  selector: 'app-user-manager',
  templateUrl: './user-manager.component.html',
  styleUrl: './user-manager.component.css'
})


export class UserManagerComponent implements OnInit {
  users: User[] = [];
  filterUser: User[] = [];
  userId: number = 0;
  firstName: string = "";
  lastName: string = "";
  email: string = "";
  address: string = "";
  phoneNumber: string = ""; 
  role: string = "";
  password: string = "";
  constructor(public admin: AdminComponent,private adminService: AdminService){}
  
  ngOnInit(): void {
    this.admin.pageTitle = 'User Management';
    this.getUsers();
  }
  //Get User list
  getUsers(){
    this.adminService.getUser().subscribe(
      (response: User[]) =>{
        this.users = response;
        this.filterUser = response
      }
    )
    
  }
  //Tìm kiếm User 
  searchUsers(): void {
    const input: string = (document.getElementById('searchUserInput') as HTMLInputElement).value.toLowerCase();
    this.filterUser = this.users.filter(user => {
        return user.fullName?.toLowerCase().includes(input) ||
            user.address?.toLowerCase().includes(input) ||
            user.phoneNumber?.toLowerCase().includes(input) ||
            user.role?.toLowerCase().includes(input) ||
            user.email?.toLowerCase().includes(input);
    });
  }
  handleKeyPress(event:any) {
    if (event.key === 'Enter') {
      this.searchUsers();
    }
  }
  // gán giá trị user edit
  openEditUser(user: User) {
    this.userId = user.userId
    this.email = user.email
    this.address = user.address
    this.phoneNumber = user.phoneNumber
    this.role = user.role
  }
  // submit user đã edit
  onSubmitEdit(form: NgForm) {
    if(form.valid) {
      this.adminService.eidtUser(this.userId, this.address, this.email, this.phoneNumber,this.role).subscribe(
        response => {
          alert("Edit Success!");
          const modalElement = document.getElementById(`editUserModal${this.userId}`);
          if (modalElement) {
            modalElement.style.display = 'none'; 
            modalElement.classList.remove('show');
          }
          const backdrop = document.querySelector('.modal-backdrop.fade.show');
          if (backdrop) {
            document.body.removeChild(backdrop);
          }
          this.getUsers();
          this.resetFormData();
        },
      );
    }
    
  }

  // submit user đã add
  errors:any=[];
  onSubmitAdd() {
    this.adminService.addUser(this.firstName, this.lastName, this.address, this.email, this.phoneNumber, this.password).subscribe(
      response => {
        alert("Add Success!");
        const modalElement = document.getElementById(`addUserModal`);
          if (modalElement) {
            modalElement.style.display = 'none'; 
            modalElement.classList.remove('show');
          }
          const backdrop = document.querySelector('.modal-backdrop.fade.show');
          if (backdrop) {
            document.body.removeChild(backdrop);
          }
        this.getUsers();
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
  // reset lại biến
  resetFormData() {
    this.firstName = "";
    this.lastName = "";
    this.email = "";
    this.address = "";
    this.phoneNumber = "";
    this.password = "";
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
  booking: Booking[];
}






