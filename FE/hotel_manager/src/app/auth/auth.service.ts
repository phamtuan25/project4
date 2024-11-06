import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  public apiUrl = 'http://localhost:8080/api/';

  constructor(private http: HttpClient) {}

  register(firstName: string, lastName: string, address: string, email: string, phoneNumber:String, password: string, role: string): Observable<any> {
    const body = {  
      firstName: firstName,
      lastName: lastName,
      address: address,
      email: email,
      phoneNumber: phoneNumber,
      password: password,
      role: role
    };
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.post(this.apiUrl + 'users', body, { headers });
  }

  login(user:any): Observable<any> {
    const body = { 
      email: user.email,
      password: user.password
    };
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.post(this.apiUrl + 'users/login', body, { headers });
  }
}
