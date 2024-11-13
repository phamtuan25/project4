import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ConfigService } from '../../../config/config.service';
import { ClientService } from '../client.service';
import { User } from '../../admin/user-manager/user-manager.component';
import { GlobalStateService } from '../../../config/global.stage.service';
import { Subscription } from 'rxjs';

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
  errors:any=[];
  private userSubscription!: Subscription;
  constructor(private router: Router, private config: ConfigService,private clientService: ClientService, private globalStageService: GlobalStateService){}
  userLogin: User | null = null

  ngOnInit(): void {
    this.userSubscription = this.globalStageService.getUserStage().subscribe(user => {
      this.userLogin = user;
    });
  }
  ngOnDestroy() {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }

  onSubmitAdd() {
    console.log(this.userLogin)
    if (!this.message || !this.userLogin?.userId) {
      alert("Message or UserId is missing")
      return; 
    }
  
    this.clientService.addContact(this.message, this.userLogin?.userId).subscribe(
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
