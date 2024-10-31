import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from './user-manager/user-manager.component';
import { ConfigService } from '../../config/config.service';
import { Room } from './room-manager/room-manager.component';
import { Provision } from './provision-manager/provision-manager.component';
import { SrvRecord } from 'dns';
import { Booking } from './booking-manager/booking-manager.component';

@Injectable({
  providedIn: 'root'
})

export class AdminService {
  public apiUrl = 'http://localhost:8080/api/';

  constructor(private http: HttpClient, private config: ConfigService) { }
  //Call Api User
  getUser() {
    const headers = this.config.getHttpHeaders();
    return this.http.get<User[]>(this.apiUrl + 'users', { headers });
  }

  addUser(firstName: string, lastName: string, address: string, email: string, phoneNumber: String, password: string): Observable<any> {
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
    return this.http.put(this.apiUrl + 'users/' + userId, body, { headers });
  }

  //Call Api Room
  getRoom() {
    const headers = this.config.getHttpHeaders();
    return this.http.get<Room[]>(this.apiUrl + 'rooms', { headers });
  }

  addRoom(roomNumber: string, roomType: string, status: string, dayPrice: number, hourPrice: number, files: File[] | null): Observable<any> {
    const params = {
      roomNumber: roomNumber,
      roomType: roomType,
      status: status,
      dayPrice: dayPrice,
      hourPrice: hourPrice
    };
    const formData = new FormData();
    formData.append('roomRequest', new Blob([JSON.stringify(params)], { type: 'application/json' }));

    if(files && files.length > 0) {
      files.forEach(file => {
        formData.append('files', file);
      });
    }
    let headers = this.config.getHttpHeaders();
    if (headers.has('Content-Type')) {
      headers = headers.delete('Content-Type');
    }
    return this.http.post(this.apiUrl + 'rooms', formData, { headers });
  }

  eidtRoom(roomId: number, roomNumber: string, roomType: string, status: string, dayPrice: number, hourPrice: number): Observable<any> {
    const body = {
      roomNumber: roomNumber,
      roomType: roomType,
      status: status,
      dayPrice: dayPrice,
      hourPrice: hourPrice,
      // images: images
    };
    const headers = this.config.getHttpHeaders();
    return this.http.put(this.apiUrl + 'rooms/' + roomId, body, { headers });
  }

   //Call Api Provision
   getProvisions() {
    const headers = this.config.getHttpHeaders();
    return this.http.get<Provision[]>(this.apiUrl + 'provisions', { headers });
  }

  addProvision(provisionName: string, description: string, price: number, status: string): Observable<any> {
    const body = {
      provisionName: provisionName,
      description: description,
      price: price,
      status: status,
      // images: images
    };
    const headers = this.config.getHttpHeaders();
    return this.http.post(this.apiUrl + 'provisions', body, { headers });
  }

  eidtProvision(provisionId: number, provisionName: string, description: string, price: number, status: string): Observable<any> {
    const body = {
      provisionName: provisionName,
      description: description,
      price: price,
      status: status,
      // images: images
    };
    const headers = this.config.getHttpHeaders();
    return this.http.put(this.apiUrl + 'provisions/' + provisionId, body, { headers });
  }

  //Call Api Booking
  getBooking() {
    const headers = this.config.getHttpHeaders();
    return this.http.get<Booking[]>(this.apiUrl + 'bookings', { headers });
  }
}

export interface Images {
  imageFileName: String;
  name: String;
  referenceId: Number;
}