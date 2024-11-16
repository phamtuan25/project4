import { HttpClient } from '@angular/common/http';
import { Injectable, Provider } from '@angular/core';
import { ConfigService } from '../../config/config.service';
import { Room } from './room/room.component';
import { Provision } from '../admin/provision-manager/provision-manager.component';
import { Observable } from 'rxjs';
import { HttpParams } from '@angular/common/http';
@Injectable({
    providedIn: 'root'
})
export class ClientService {
    public apiUrl = 'http://localhost:8080/api/';

    constructor(private http: HttpClient, private config: ConfigService) { }

    getUserLogin(email: string | null) {
        const headers = this.config.getHttpHeaders();
        return this.http.post(this.apiUrl + 'users/userLogin', email, { headers })
    }
    //Call Api Room
    getRooms(page: number, size: number, keyword: string, status?: string) {
        const headers = this.config.getHttpHeaders();
        var params = `page=${page}&size=${size}&keyword=${keyword}`;
        if (status) {
            params = params + `&status=${status}`;
        }
        return this.http.get<Room[]>(`${this.apiUrl}rooms?${params}`, { headers });
    }

    getRoomsHome(page: number, size: number, keyword: string, status?: string, roomType?: string) {
        const headers = this.config.getHttpHeaders();
        let params = `page=${page}&size=${size}&keyword=${keyword}`;
        if (status) {
            params += `&status=${status}`;
        }
        if (roomType) {
            params += `&roomType=${roomType}`;
        }
    
        return this.http.get<Room[]>(`${this.apiUrl}rooms?${params}`, { headers });
    }

    getRoomDetail(roomId: number) {
        const headers = this.config.getHttpHeaders();
        return this.http.get<Room>(`${this.apiUrl}rooms/${roomId}`, { headers });
    }

    //Call Api Provision
    getProvision(page: number, size: number, keyword: string, status?: string) {
        const headers = this.config.getHttpHeaders();
        var params = `page=${page}&size=${size}&keyword=${keyword}`;
        if (status) {
            params = params + `&status=${status}`;
        }
        return this.http.get<Provision[]>(`${this.apiUrl}provisions?${params}`, { headers });
    }
    
    
    getProvisionBooking(status?: string) {
        const headers = this.config.getHttpHeaders();
        return this.http.get<Provision[]>(`${this.apiUrl}provisions`, { headers });
    }

    //Call Api Contact
    addContact(message: string, userId: number): Observable<any> {
        const body = {
            message: message,
            userId: userId
        };
        const headers = this.config.getHttpHeaders();
        return this.http.post(this.apiUrl + 'contacts', body, { headers });
    }
    //Call api booking
    addBooking(userId: number | undefined, bookingDetailResquest: any): Observable<any> {

        const body = {
            bookingDetailRequests: bookingDetailResquest,
            user: {
                userId: userId
            }
        };

        const headers = this.config.getHttpHeaders();
        console.log('body', body)
        return this.http.post('http://localhost:8080/api/bookings', body, { headers });
    }

    getBookingByUser(userId: number | undefined): Observable<any> {
        const headers = this.config.getHttpHeaders();
        return this.http.get(this.apiUrl + 'bookings/' + userId, { headers });
    }

    //Call api user
    editUser(userId: number, address: string, email: string, phoneNumber: string, role: string): Observable<any> {
        const body = {
            address: address,
            email: email,
            phoneNumber: phoneNumber,
            role: role
        };
        const headers = this.config.getHttpHeaders();
        return this.http.put(this.apiUrl + 'users/' + userId, body, { headers });
    }

    changePassword(userId: number, currentPassword: string, newPassword: string, confirmPassword: string): Observable<any> {
        const body = {
            currentPassword: currentPassword,
            newPassword: newPassword,
            confirmPassword: confirmPassword
        };
        const headers = this.config.getHttpHeaders();
        return this.http.put(this.apiUrl + 'users/changePassword/' + userId, body, { headers });
    }

    //Call check room

    getAvailableRooms(checkin: string, checkout: string, roomType: string): Observable<any> {
        const body = {
            checkIn: checkin,
            checkOut: checkout,
            roomType: roomType
          };

          const headers = this.config.getHttpHeaders();
          return this.http.post(this.apiUrl + 'rooms/available', body, { headers });
    }
}