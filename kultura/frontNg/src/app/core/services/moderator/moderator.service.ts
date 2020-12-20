import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {User} from '../../models/user';
import {HttpClient} from '@angular/common/http';
import { ModeratorsPage} from '../../models/moderatorsPage';

@Injectable({
  providedIn: 'root'
})
export class ModeratorService {
  moderators: any;
  constructor(private httpClient: HttpClient) {
  }

  public getModerators(): Observable<ModeratorsPage>{
    return this.httpClient.get('/api/users');
  }

  info(): void {
    console.log('DJES PEROOOOOO');
    return ;
  }
}
