import { Component } from '@angular/core';
import { AdminComponent } from '../admin.component';
import { AdminService } from '../admin.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-contact-manager',
  templateUrl: './contact-manager.component.html',
  styleUrl: './contact-manager.component.css'
})
export class ContactManagerComponent {
  contacts : Contact[]= [];
  isShowAddPopup: Boolean = false;
  isShowEditPopup: Boolean = false;
  contactId: number = 0;
  email: string = "";
  fullName: string = "";
  message: string = "";
  status: string = "";
  totalContacts: number = 0;
  pageSize: number = 10;
  currentPage: number = 1;
  totalPages: number = 0;
  keyword: string = '';

  constructor(public admin: AdminComponent,private adminService: AdminService){}
  ngOnInit(): void {
    this.admin.pageTitle = 'Contact Management';
    this.getContacts(this.currentPage , this.pageSize, this.keyword);
  }
  //Get Contact list
  getContacts(page: number, size: number, keyword: string) {
    this.adminService.getContact(page - 1, size, keyword).subscribe(
      (response: any) => {
        this.contacts = response.content; 
        this.totalPages = Math.ceil(this.totalContacts / this.pageSize);
        console.log('Fetched contacts:', this.contacts); 
      },
      (error) => {
        console.error("Error fetching contacts", error);
        this.contacts = [];  
      }
    );
  }
  goToPage(page: number): void {
    if (page < 1 || page > Math.ceil(this.totalContacts / this.pageSize)) return;
    this.currentPage = page;
    this.getContacts(this.currentPage, this.pageSize, this.keyword);
  }
  //Tìm kiếm Contact 
  searchContacts(): void {
    const input: string = (document.getElementById('searchContactsInput') as HTMLInputElement).value.trim();
    this.keyword = input;
    this.currentPage = 1;
    this.getContacts(this.currentPage, this.pageSize, this.keyword);
  }
  handleKeyPress(event: any) {
    if (event.key === 'Enter') {
      this.searchContacts();
    }
  }
  openEditContact(contact: Contact) {
    this.contactId = contact.contactId
    this.email = contact.userResponse.email
    this.fullName = contact.userResponse.fullName
    this.message = contact.message
    this.status = contact.status
    this.isShowEditPopup = true;
    this.openPopup();
  }

  onSubmitEdit(form: NgForm) {
    console.log(form.valid)
    if (form.valid) {
      this.adminService.editContact(this.contactId, this.status).subscribe(
        response => {
          alert("Edit Success!");
          this.getContacts(this.currentPage , this.pageSize, this.keyword);
          this.resetFormData();
        },
      );
    }
  }
  resetFormData() {
    this.message = "";
    this.status = "";
    this.isShowAddPopup = false;
    this.isShowEditPopup = false;
    this.closePopup();
  }

  isObject(value: any) {
    return typeof value === 'object' && value !== null && !Array.isArray(value);
  }
  openPopup() {
    document.body.style.paddingRight = '17px'
    document.body.classList.add('modal-open')
      
  }
  closePopup() {
    document.body.style.paddingRight = '';
    document.body.classList.remove('modal-open')
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