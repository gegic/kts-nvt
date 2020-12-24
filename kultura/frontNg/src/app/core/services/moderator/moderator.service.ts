import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {ModeratorsPage} from '../../models/moderatorsPage';
import {Moderator} from '../../models/moderator';

@Injectable({
  providedIn: 'root'
})
export class ModeratorService {

  constructor(private httpClient: HttpClient) {
  }

  public getModerators(): Observable<ModeratorsPage> {
    return this.httpClient.get('/api/users/moderators');
  }

  public createModerator(moderator: Moderator): Observable<any>{
    return  this.httpClient.post('/api/users/moderator', moderator);
  }
}
