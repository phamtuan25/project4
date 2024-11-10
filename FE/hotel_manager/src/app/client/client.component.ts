import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ConfigService } from '../../config/config.service';
import { ClientService } from './client.service';
import { User } from '../admin/user-manager/user-manager.component';

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrl: './client.component.css'
})
export class ClientComponent {
  title = 'hotel_manager';
  pageTitle = 'Client Page';
  constructor(private router: Router, private config: ConfigService,private clientService: ClientService){}
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
    this.clientService.getUserLogin(email).subscribe(
      (response: any) => {
        this.userLogin = response;
      },
      (error) => {
        console.error("Error fetching users", error);
      }
    )
  }
}