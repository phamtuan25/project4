import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ConfigService } from '../config/config.service';
import {Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  title = 'hotel_manager';
  public apiUrl = 'http://localhost:8080/api/';
  constructor(private config: ConfigService, private router: Router, private http: HttpClient) { }
  ngOnInit(): void {
    const token = this.config.getToken();
    if(!token) {
      this.router.navigate(['/login']);
    }  
  }
  
}
