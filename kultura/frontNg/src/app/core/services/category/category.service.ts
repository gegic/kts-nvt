import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Page} from '../../models/Page';
import {Moderator} from '../../models/moderator';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(private httpClient: HttpClient) {
  }

  public getCategories(): Observable<Page> {
    return this.httpClient.get('/api/categories');
  }

  // public createCategory(category: Category): Observable<any>{
  //   return  this.httpClient.post('/api/category', category);
  // }

}
