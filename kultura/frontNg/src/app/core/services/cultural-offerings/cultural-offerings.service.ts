import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {CulturalOffering} from '../../models/cultural-offering';
import {Category} from '../../models/category';
import {Subcategory} from '../../models/subcategory';

@Injectable({
  providedIn: 'root'
})
export class CulturalOfferingsService {

  culturalOfferings: CulturalOffering[] = [];
  searchQuery: BehaviorSubject<string> = new BehaviorSubject<string>('');
  rating = {min: 1, max: 5};
  selectedCategory?: Category;
  selectedSubcategory?: Subcategory;
  noReviews = true;
  isLocationRelative = false;
  filterByLocation = false;
  locationDistance = 2;
  latitudeStart?: number;
  latitudeEnd?: number;
  longitudeStart?: number;
  longitudeEnd?: number;


  categories?: Category[] = [];
  subcategories?: Subcategory[] = [];

  constructor(private httpClient: HttpClient) { }

  getCulturalOfferings(page: number, sort: string): Observable<any> {
    let apiUrl = `/api/cultural-offerings?page=${page}&sort=${sort}`;
    if (!!this.searchQuery) {
      apiUrl += `&search=${this.searchQuery.getValue()}`;
    }
    if (!!this.rating) {
      apiUrl += `&rating-min=${this.rating.min}&rating-max=${this.rating.max}`;
    }
    return this.httpClient.get(apiUrl);
  }

  getRecommendations(address: string): Observable<any> {
    return this.httpClient.get(this.queryLocation(address));
  }

  getCategories(lastLoadedPage: number): Observable<any> {
    return this.httpClient.get(`/api/categories?page=${lastLoadedPage}`);
  }

  getSubcategories(categoryId: number, lastLoadedPage: number): Observable<any> {
    return this.httpClient.get(`/api/subcategories/category/${categoryId}?page=${lastLoadedPage}`);
  }

  delete(id: number): Observable<any> {
    return this.httpClient.delete(`/api/cultural-offerings/${id}`);
  }

  private queryLocation(address: string): string {
    return `https://nominatim.openstreetmap.org/search?format=jsonv2&q=${address}&limit=4`;
  }
}
