import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ConfigService } from '../../config/config.service';
import { ClientService } from './client.service';
import { User } from '../admin/user-manager/user-manager.component';
import { GlobalStateService } from '../../config/global.stage.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrl: './client.component.css'
})
export class ClientComponent {
  title = 'hotel_manager';
  pageTitle = 'Client Page';
  constructor(private router: Router, private config: ConfigService,private clientService: ClientService, private globalStageService: GlobalStateService){}
  userLogin: User | null = null
  signOut(): void {
    if (confirm("Are you sure you want to log out?")) {
      this.config.setToken(''); 
      this.config.setEmail('');  
      this.userLogin = null;   
      alert("Signed out!");
      this.router.navigate(['/login']);
    }
  }
  ngOnInit(): void {
    const email = this.config.getEmail();
    if(email) this.getUserLogin(email);
   
  }

  getInitial(fullName: string | null | undefined): string {
    if (!fullName) {
      return 'N/A'; 
    }
    return fullName.charAt(0).toUpperCase();
  }
  
  openProfile() {
  }
  getUserLogin(email: string | null){
    this.clientService.getUserLogin(email).subscribe(
      (response: any) => {
        this.userLogin = response;
        this.globalStageService.setUserStage(response);
      },
      (error) => {
        console.error("Error fetching users", error);
      }
    )
  }
}
