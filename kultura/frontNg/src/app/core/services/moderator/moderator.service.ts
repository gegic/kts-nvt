import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Page} from '../../models/Page';
import {Moderator} from '../../models/moderator';
import {User} from '../../models/user';

@Injectable({
  providedIn: 'root'
})
export class ModeratorService {

  constructor(private httpClient: HttpClient) {
  }

  public getModerators(): Observable<Page> {
    return this.httpClient.get('/api/users/moderators');
  }

  public getModeratorById(id: number): Observable<Moderator> {
    return this.httpClient.get('/api/users/' + id);
  }

  public createModerator(moderator: Moderator): Observable<any>{
    return  this.httpClient.post('/api/users/moderator', moderator);
  }

  public updateModerator(moderator: Moderator): Observable<any>{
    console.log('ModeratorService update moderator');
    console.log(moderator);
    return this.httpClient.put('/api/users', moderator);
  }

  delete(id: string | number): Observable<any> {
    return this.httpClient.delete(`/api/users/${id}`);
  }
}
