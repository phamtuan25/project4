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
  getUsers(){
    this.adminService.getUser().subscribe(
      (response: User[]) =>{
        this.users = response;
        this.filterUser = response
      }
    )
    
  }
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
  openEditUser(user: User) {
    this.userId = user.userId
    this.email = user.email
    this.address = user.address
    this.phoneNumber = user.phoneNumber
    this.role = user.role
  }
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
        },
        error => {
          console.error('Edit failed:', error);
        }
      );
    }
    
  }
  errors:any=[];
  onSubmitAdd() {
    this.adminService.addUser(this.firstName, this.lastName, this.address, this.email, this.phoneNumber, this.password).subscribe(
      response => {
        this.errors = [];
      },
      error => {
        this.errors = [];
        console.log(this.errors);
        error.error.forEach((element:any) => {
          this.errors.push(element);
        });
        console.log(this.errors);
        console.error('Add User failed:', error);
      }
    );
  }
  findErrors(key:string){
    return this.errors.find((error:any)=>error.key==key)?.message;
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


function submitUser(): void {
  const userName: string = (document.getElementById('userName') as HTMLInputElement).value.trim();
  const userAddress: string = (document.getElementById('userAddress') as HTMLInputElement).value.trim();
  const userPhone: string = (document.getElementById('userPhone') as HTMLInputElement).value.trim();
  const userRole: string = (document.getElementById('userRole') as HTMLInputElement).value.trim();
  const userEmail: string = (document.getElementById('userEmail') as HTMLInputElement).value.trim();

  if (!userName || !userAddress || !userPhone || !userRole || !userEmail) {
      alert("Vui lòng điền đầy đủ thông tin!");
      return;
  }

  alert(`Đã thêm người dùng: ${userName}, Vai trò: ${userRole}`);
  $('#addUserModal').modal('hide');
  (document.getElementById('addUserForm') as HTMLFormElement).reset();
}

function openEditUserModal(id: string, name: string, address: string, phone: string, role: string, email: string): void {
  (document.getElementById('editUserId') as HTMLInputElement).value = id;
  (document.getElementById('editUserName') as HTMLInputElement).value = name;
  (document.getElementById('editUserAddress') as HTMLInputElement).value = address;
  (document.getElementById('editUserPhone') as HTMLInputElement).value = phone;
  (document.getElementById('editUserRole') as HTMLInputElement).value = role;
  (document.getElementById('editUserEmail') as HTMLInputElement).value = email;
  $('#editUserModal').modal('show');
}

function submitEditUser(): void {
  const id: string = (document.getElementById('editUserId') as HTMLInputElement).value;
  const userName: string = (document.getElementById('editUserName') as HTMLInputElement).value.trim();
  const userAddress: string = (document.getElementById('editUserAddress') as HTMLInputElement).value.trim();
  const userPhone: string = (document.getElementById('editUserPhone') as HTMLInputElement).value.trim();
  const userRole: string = (document.getElementById('editUserRole') as HTMLInputElement).value.trim();
  const userEmail: string = (document.getElementById('editUserEmail') as HTMLInputElement).value.trim();

  if (!userName || !userAddress || !userPhone || !userRole || !userEmail) {
      alert("Vui lòng điền đầy đủ thông tin!");
      return;
  }

  alert(`Đã sửa người dùng: ID: ${id}, Tên: ${userName}, Vai trò: ${userRole}`);
  $('#editUserModal').modal('hide');
  (document.getElementById('editUserForm') as HTMLFormElement).reset();
}

function deleteUser(id: string): void {
  if (confirm(`Bạn có chắc chắn muốn xóa người dùng với ID: ${id}?`)) {
      alert(`Người dùng với ID ${id} đã bị xóa!`);
      // Thêm logic để xóa người dùng từ bảng
  }
}



