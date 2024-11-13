import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ConfigService } from '../config/config.service';
import {Router } from '@angular/router';
import { User } from './admin/user-manager/user-manager.component';
import { ClientService } from './client/client.service';
import { GlobalStateService } from '../config/global.stage.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  title = 'hotel_manager';
  public apiUrl = 'http://localhost:8080/api/';
  userLogin: User | null = null
  constructor(private config: ConfigService, private router: Router, private http: HttpClient, private clientService: ClientService, private globalStageService: GlobalStateService) { }
  ngOnInit(): void {
    const email = this.config.getEmail();
    if(email) this.getUserLogin(email);
  }
  getUserLogin(email: string | null){
    this.clientService.getUserLogin(email).subscribe(
      (response: any) => {
        this.globalStageService.setUserStage(response);
      },
      (error) => {
        console.error("Error fetching users", error);
      }
    )
  }
}
