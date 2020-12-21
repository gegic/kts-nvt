import {Injectable} from '@angular/core';
import {Category} from '../../models/category';
import {Subcategory} from '../../models/subcategory';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CulturalOffering} from '../../models/cultural-offering';
import {CulturalOfferingPhoto} from '../../models/culturalOfferingPhoto';

@Injectable({
  providedIn: 'root'
})
export class AddOfferingService {

  categories?: Category[] = [];
  subcategories?: Subcategory[] = [];
  private culturalOffering: CulturalOffering = new CulturalOffering();

  constructor(private httpClient: HttpClient) {
  }

  get coordinates(): [number, number] {
    return [this.culturalOffering.latitude ?? 0, this.culturalOffering.longitude ?? 0];
  }

  set coordinates(latLng: [number, number]) {
    this.culturalOffering.latitude = latLng[0];
    this.culturalOffering.longitude = latLng[1];
  }

  set name(val: string) {
    this.culturalOffering.name = val;
  }

  set address(val: { display_name: string }) {
    this.culturalOffering.address = val.display_name;
  }

  set briefInfo(val: string) {
    this.culturalOffering.briefInfo = val;
  }

  set subcategory(val: Subcategory) {
    this.culturalOffering.subcategoryId = val.id;
  }

  set additionalInfo(val: string) {
    this.culturalOffering.additionalInfo = val;
  }

  set photo(val: CulturalOfferingPhoto) {
    this.culturalOffering.photoId = val.id;
  }

  getCategories(lastLoadedPage: number): Observable<any> {
    return this.httpClient.get(`/api/categories?page=${lastLoadedPage}`);
  }

  getSubcategories(categoryId: number, lastLoadedPage: number): Observable<any> {
    return this.httpClient.get(`/api/subcategories/category/${categoryId}?page=${lastLoadedPage}`);
  }

  clearPhotos(): void {
    this.httpClient.delete('/api/cultural-offerings/clear-photos').subscribe();
  }

  addPhoto(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('photo', file);
    return this.httpClient.post('/api/cultural-offerings/add-photo', formData);
  }

  addOffering(): Observable<any> {
    console.log(this.culturalOffering);
    return this.httpClient.post('/api/cultural-offerings', this.culturalOffering);
  }
}
