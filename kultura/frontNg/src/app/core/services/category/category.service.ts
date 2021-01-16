import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Page} from '../../models/Page';
import {Moderator} from '../../models/moderator';
import {CulturalOffering} from '../../models/cultural-offering';
import {Category} from '../../models/category';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  categoriesList: Category[] = [];

  constructor(private httpClient: HttpClient) {
  }

  public getCategories(): Observable<Page> {
    return this.httpClient.get('/api/categories');
  }

  public checkExists(categoryName: string): boolean {
    this.getCategories().subscribe(categories => {
      this.categoriesList = categories.content || [];
    });
    for (const category of this.categoriesList){
      if (category.name?.toUpperCase() === categoryName.toUpperCase()) {
        return true;
      }
    }

    return false;
  }

  public createCategory(category: Category): Observable<any>{
    console.log('CateogryService create category');
    console.log(category);
    return this.httpClient.post('/api/categories', category);
  }

  public delete(id: string | number): Observable<any> {
    return this.httpClient.delete(`/api/categories/${id}`);
  }

}
