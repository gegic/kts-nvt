import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Page} from '../../models/Page';
import {Moderator} from '../../models/moderator';
import {Category} from '../../models/category';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  categories: Category[] = [];

  constructor(private httpClient: HttpClient) {
  }

  public getCategories(page: number): Observable<any> {
    return this.httpClient.get(`/api/categories?page=${page}`);
  }

  delete(id: number): Observable<any> {
    return this.httpClient.delete(`/api/categories/${id}`);
  }
}
