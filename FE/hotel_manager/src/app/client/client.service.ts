import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from '../../config/config.service';
import { Room } from './room/room.component';
@Injectable({
    providedIn: 'root'
})
export class ClientService {
    public apiUrl = 'http://localhost:8080/api/';

    constructor(private http: HttpClient, private config: ConfigService) { }

    //Call Api Room
    getRooms(page: number, size: number, keyword: string, status?: string) {
        const headers = this.config.getHttpHeaders();
        var params = `page=${page}&size=${size}&keyword=${keyword}`;
        if (status) {
            params = params + `&status=${status}`;
        }
        return this.http.get<Room[]>(`${this.apiUrl}rooms?${params}`, { headers });
    }

    getRoomDetail(roomId: number) {
        const headers = this.config.getHttpHeaders();
        return this.http.get<Room>(`${this.apiUrl}rooms/${roomId}`, { headers });
    }
}