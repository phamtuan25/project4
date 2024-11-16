import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ConfigService } from '../../../config/config.service';
import { ClientService } from '../client.service';
import { NgForm } from '@angular/forms';
import Modal from 'bootstrap/js/dist/modal';
import { GlobalStateService } from '../../../config/global.stage.service';
import { Subscription } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent implements OnInit {
  editUserForm!: FormGroup;
  userLogin: User | null = null;
  users: User[] = [];
  errors:any=[];
  userId: number = 0;
  email: string = "";
  address: string = "";
  phoneNumber: string = "";
  fullName: string = "";
  role: string = "CUSTOMER";
  currentPassword: string = '';
  newPassword: string = '';
  confirmPassword: string = '';
  editModal!: Modal;
  changePasswordModal!: Modal;
  private userSubscription!: Subscription;
  
  constructor(private router: Router, private config: ConfigService, private clientService: ClientService, private globalStageService: GlobalStateService,private fb: FormBuilder) { }

  ngOnInit(): void {
    this.userSubscription = this.globalStageService.getUserStage().subscribe(user => {
      this.userLogin = user;
    });
    this.editUserForm = this.fb.group({
      phoneNumber: [
        '', 
        [
          Validators.required, // Trường bắt buộc
          Validators.pattern(/^(\+84|0)[0-9]{9,10}$/) // Regex kiểm tra định dạng số điện thoại
        ]
      ],
      address: [
        '',
        [Validators.required]
      ]
    });
  }
  ngOnDestroy() {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }
  ngAfterViewInit() {
    this.editModal = new Modal('#editModal', {
      backdrop: 'static'
    });
    this.changePasswordModal = new Modal('#changePasswordModal', {
      backdrop: 'static'
    });
  }
  openEditUser(user: User | null) {
    if (user) {
      this.userId = user.userId;
      this.fullName = user.fullName;
      this.email = user.email;
      this.phoneNumber = user.phoneNumber;
      this.address = user.address;
      this.editModal.show();
    } else {
      console.error("User data is null");
    }
  }
  // submit user đã edit
  onSubmitEdit() {
    if (this.editUserForm.valid) {
      this.role = "CUSTOMER";  
      this.clientService.editUser(this.userId, this.address, this.email, this.phoneNumber, this.role).subscribe(
        response => {
          this.userLogin = response;
          
          alert("Edit Success!");
          this.resetFormData();
        },
        (error) => {
          console.error("Error updating user:", error); 
          alert("Edit failed. Please try again.");
        }
      );
    }
  }
  
  // reset lại biến
  resetFormData() {
    this.email = "";
    this.address = "";
    this.phoneNumber = "";
    this.editModal.hide();
  }
  openChangePassword(): void {
    if (this.userLogin) {
      this.userId = this.userLogin.userId; 
      this.changePasswordModal.show();
    } else {
      console.error("User login data is not available");
    }
  }
  
  onSubmitChangePassword(form: NgForm){
    if (form.valid) {
      this.clientService.changePassword(this.userId, this.currentPassword, this.newPassword, this.confirmPassword).subscribe(
        response => {
          console.log('Password changed successfully:', response);
          alert('Password changed successfully!');
          this.resetPasswordFormData();
        },
        (error) => {
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
  }
  findErrors(key:string){
    return this.errors.find((error:any)=>error.key==key)?.message;
  }
  isObject(value: any) {
    return typeof value === 'object' && value !== null && !Array.isArray(value);
  }
  resetPasswordFormData(): void {
    this.currentPassword = '';
    this.newPassword = '';
    this.confirmPassword = '';
    this.errors = '';
    this.changePasswordModal.hide();
  }
}
export interface User {
  userId: number;
  fullName: string;
  email: string;
  address: string;
  phoneNumber: string;
}