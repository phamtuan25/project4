import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from './user-manager/user-manager.component';
import { ConfigService } from '../../config/config.service';

@Injectable({
  providedIn: 'root'
})

export class AdminService {
  public apiUrl = 'http://localhost:8080/api/';

  constructor(private http: HttpClient, private config: ConfigService) { }
  getUser() {
    const headers = this.config.getHttpHeaders();
    return this.http.get<User[]>(this.apiUrl + 'users', { headers });
  }

  addUser(firstName: string, lastName: string, address: string, email: string, phoneNumber:String, password: string): Observable<any> {
    const body = {  
      firstName: firstName,
      lastName: lastName,
      address: address,
      email: email,
      phoneNumber: phoneNumber,
      password: password
    };
    const headers = this.config.getHttpHeaders();
    return this.http.post(this.apiUrl + 'users', body, { headers });
  }

  eidtUser(userId: number, address: string, email: string, phoneNumber: string, role: string): Observable<any> {
    const body = {
      address: address,
      email: email,
      phoneNumber: phoneNumber,
      role: role
    };
    const headers = this.config.getHttpHeaders();
    return this.http.put(this.apiUrl + 'users/'+ userId, body, { headers });
  }
}