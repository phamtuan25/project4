import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ConfigService } from '../../../config/config.service';
import { ClientService } from '../client.service';
import { User } from '../../admin/user-manager/user-manager.component';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrl: './contact.component.css'
})
export class ContactComponent {
  title = 'hotel_manager';
  pageTitle = 'Client Page';
  contacts: Contact[] = [];
  message: string = ""; 
  userId: number = 0;
  errors:any=[];
  constructor(private router: Router, private config: ConfigService,private clientService: ClientService){}
  userLogin: User | null = null

  ngOnInit(): void {
    const email = this.config.getEmail();
    console.log(email)
    this.getUserLogin(email);
  }
  getUserLogin(email: string | null){
    this.clientService.getUserLogin(email).subscribe(
      (response: any) => {
        this.userLogin = response;
        this.userId = response.userId;
      },
      (error) => {
        console.error("Error fetching users", error);
      }
    )
  }

  onSubmitAdd() {
    if (!this.message || !this.userId) {
      console.error("Message or UserId is missing");
      return; 
    }
  
    this.clientService.addContact(this.message, this.userId).subscribe(
      response => {
        alert("Add Success!");
        this.resetFormData();
      },
      error => {
        this.errors = [];
        if (this.isObject(error.error)) {
          this.errors.push(error.error);
        } else if (Array.isArray(error.error)) {
          error.error.forEach((element: any) => {
            this.errors.push(element);
          });
        } else {
          console.error("Unexpected error structure", error.error);
        }
        console.log("error", error.error);
      }
    );
  }
  resetFormData(){
    this.message = "";
  }
  findErrors(key:string){
    return this.errors.find((error:any)=>error.key==key)?.message;
  }
  isObject(value: any) {
    return typeof value === 'object' && value !== null && !Array.isArray(value);
  }
}
export interface Contact {
  contactId: number;
  userResponse: UserResponse;
  message: string
  status: string; 
  createdAt: string;
  updatedAt: string;
}
export interface UserResponse {
  email: string; 
  fullName: string;
}
