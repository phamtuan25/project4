import { HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'hotel_manager';
  private tokenKey = 'token';
  getToken(){
    return localStorage.getItem(this.tokenKey);
  }

  setToken(token: string){
    localStorage.setItem(this.tokenKey, token);
  }

  httpHeader: any;
}
