import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ConfigService } from '../../config/config.service';
import { User } from './user-manager/user-manager.component';
import { AdminService } from './admin.service';
import { GlobalStateService } from '../../config/global.stage.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent implements OnInit{
  title = 'hotel_manager';
  pageTitle = 'Management Page';
  constructor(private router: Router, private config: ConfigService, private adminService: AdminService, private globalStageService: GlobalStateService){}
  userLogin: User | null = null
  signOut(): void {
    if (confirm("Are you sure you want to log out?")) {
        alert("Signed out!");
        this.router.navigate(['/login'])
        this.config.setToken(''); 
    }
  }
  getUserLogin(email: string | null){
    this.adminService.getUserLogin(email).subscribe(
      (response: any) => {
        this.userLogin = response;
        this.globalStageService.setUserStage(response);
        if(this.config.getToken() && response?.role == 'CUSTOMER'){
      
          this.router.navigate(['/']).then(() => {
            window.location.reload(); 
          });
        }
      },
      (error) => {
        console.error("Error fetching users", error);
      }
    )
  }
  ngOnInit(): void {
    const email = this.config.getEmail();
    if(email) this.getUserLogin(email);
    const token = this.config.getToken();
    if(!token) {
      this.router.navigate(['/login']);
    }
  }
  goToHomePage(){
    this.router.navigate(['/']).then(() => {
      window.location.reload();
    });
  }

}


