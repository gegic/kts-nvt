import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SubcategoryService {

  constructor(private httpClient: HttpClient) { }

  public getSubcategories(categoryId: number | undefined): Observable<any> {
    return this.httpClient.get(`/api/subcategories/category/${categoryId}`);
  }

  public delete(id: string | number): Observable<any> {
    return this.httpClient.delete(`/api/subcategories/${id}`);
  }
}
