import { Injectable } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  private tokenKey = 'token';

  constructor() {}

  // Phương thức để lấy token từ localStorage
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  // Phương thức để set token vào localStorage
  setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  getEmail(): string | null{
    return localStorage.getItem('email');
  }
  setEmail(email: string): void{
    return localStorage.setItem('email', email);
  }
  // Phương thức để lấy httpHeader
  getHttpHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + token
    });
  }
}
