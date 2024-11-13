import { Component, OnInit } from '@angular/core';
import { ClientService } from '../client.service';
import { ConfigService } from '../../../config/config.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  constructor(private client: ClientService,private router: Router) { }
  ngOnInit(): void {
  }
}
