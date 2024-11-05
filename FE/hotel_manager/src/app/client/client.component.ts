import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrl: './client.component.css'
})
export class ClientComponent {
  title = 'hotel_manager';
  pageTitle = 'Management Page';
  constructor(private router: Router){}
  signOut(): void {
    if (confirm("Are you sure you want to log out?")) {
        alert("Signed out!");
        this.router.navigate(['']) 
    }
  }
}