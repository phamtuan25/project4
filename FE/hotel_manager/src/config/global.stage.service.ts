import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../app/admin/user-manager/user-manager.component';

@Injectable({
  providedIn: 'root'
})
export class GlobalStateService {
  private userStage = new BehaviorSubject<User | null>(null);

  setUserStage(newState: any) {
    this.userStage.next(newState);
  }

  getUserStage(): Observable<any> {
    return this.userStage.asObservable();
  }
}