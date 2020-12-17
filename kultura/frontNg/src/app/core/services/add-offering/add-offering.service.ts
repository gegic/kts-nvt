import { Injectable } from '@angular/core';
import {Category} from '../../models/category';
import {Subcategory} from '../../models/subcategory';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CulturalOffering} from '../../models/cultural-offering';

@Injectable({
  providedIn: 'root'
})
export class AddOfferingService {

  private culturalOffering: CulturalOffering = new CulturalOffering();

  categories?: Category[] = [];
  subcategories?: Subcategory[] = [];

  constructor(private httpClient: HttpClient) { }

  getCategories(lastLoadedPage: number): Observable<any> {
    return this.httpClient.get(`/api/categories?page=${lastLoadedPage}`);
  }

  getSubcategories(categoryId: number, lastLoadedPage: number): Observable<any> {
    return this.httpClient.get(`/api/subcategories/category/${categoryId}?page=${lastLoadedPage}`);
  }

  addPhoto(file: File): Observable<any>{
    const formData = new FormData();
    formData.append('photo', file);
    return this.httpClient.post('/api/cultural-offerings/add-photo', formData);
  }

  set coordinates(latLng: [number, number]) {
    this.culturalOffering.latitude = latLng[0];
    this.culturalOffering.longitude = latLng[1];
  }

  get coordinates(): [number, number] {
    return [this.culturalOffering.latitude ?? 0, this.culturalOffering.longitude ?? 0];
  }

}
