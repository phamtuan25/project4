import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ConfigService } from '../../config/config.service';
import { User } from './user-manager/user-manager.component';
import { AdminService } from './admin.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent implements OnInit{
  title = 'hotel_manager';
  pageTitle = 'Management Page';
  constructor(private router: Router, private config: ConfigService, private adminService: AdminService){}
  userLogin: User | null = null
  signOut(): void {
    if (confirm("Are you sure you want to log out?")) {
        alert("Signed out!");
        this.router.navigate(['/login'])
        this.config.setToken(''); 
    }
  }
  ngOnInit(): void {
    const email = this.config.getEmail();
    console.log(email)
    this.getUserLogin(email);
  }
  getUserLogin(email: string | null){
    this.adminService.getUserLogin(email).subscribe(
      (response: any) => {
        this.userLogin = response;
      },
      (error) => {
        console.error("Error fetching users", error);
      }
    )
  }
}


