import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'https://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  register(firstName: string, lastName: string, address: string, email: string, phoneNumber:String, password: string): Observable<any> {
    const body = {firstName, lastName, address, email,phoneNumber, password };
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.post(this.apiUrl, body, { headers });
  }
}
