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
  private userSubscription!: Subscription;
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
    this.userSubscription = this.globalStageService.getUserStage().subscribe(user => {
      this.userLogin = user;
    });
  }
  ngOnDestroy() {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }
  getInitial(fullName: string | null | undefined): string {
    if (!fullName) {
      return 'N/A'; 
    }
    return fullName.charAt(0).toUpperCase();
  }
  
  openProfile() {
  }
}
