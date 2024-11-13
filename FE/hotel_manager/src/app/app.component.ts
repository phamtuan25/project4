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
  ngOnInit(): void {
  }
}
