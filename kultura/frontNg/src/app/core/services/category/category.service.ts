import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Page} from '../../models/Page';
import {Moderator} from '../../models/moderator';
import {Category} from '../../models/category';
import {Subcategory} from '../../models/subcategory';

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

  create(category: Category): Observable<any> {
    return this.httpClient.post('/api/categories', category);
  }

  update(category: Category): Observable<any> {
    return this.httpClient.put('/api/categories', category);
  }

  getSubcategories(categoryId: number, page: number): Observable<any> {
    return this.httpClient.get(`/api/subcategories/category/${categoryId}?page=${page}`);
  }

  updateSubcategory(subcategory: Subcategory): Observable<any> {
    return this.httpClient.put('/api/subcategories', subcategory);
  }

  createSubcategory(subcategory: Subcategory): Observable<any> {
    return this.httpClient.post('/api/subcategories', subcategory);
  }

  deleteSubcategory(id: number): Observable<any> {
    return this.httpClient.delete(`/api/subcategories/${id}`);
  }
}
