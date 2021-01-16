import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {CulturalOffering} from '../../models/cultural-offering';
import {Category} from '../../models/category';
import {Subcategory} from '../../models/subcategory';
import * as L from 'leaflet';

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
  absoluteAddress = '';
  absolutePosition?: [number, number];


  categories?: Category[] = [];
  subcategories?: Subcategory[] = [];

  constructor(private httpClient: HttpClient) { }

  getCulturalOfferings(page: number, sort: string): Observable<any> {
    let apiUrl = `/api/cultural-offerings?page=${page}&sort=${sort}&no-reviews=${this.noReviews}`;
    if (!!this.searchQuery) {
      apiUrl += `&search=${this.searchQuery.getValue()}`;
    }
    if (!!this.rating) {
      apiUrl += `&rating-min=${this.rating.min}&rating-max=${this.rating.max}`;
    }
    if (!!this.selectedCategory) {
      apiUrl += `&category=${this.selectedCategory.id}`;
    }
    if (!!this.selectedSubcategory) {
      apiUrl += `&subcategory=${this.selectedSubcategory.id}`;
    }
    if (!!this.absoluteAddress && !!this.latitudeStart && !!this.latitudeEnd && !!this.longitudeStart && !!this.longitudeEnd) {
      apiUrl += `&lng-start=${this.longitudeStart}&lng-end=${this.longitudeEnd}` +
        `&lat-start=${this.latitudeStart}&lat-end=${this.latitudeEnd}`;
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

  getRelativeAddress(position: any[]): Observable<any> {
    return this.httpClient.get(this.reverseGeocoding(position));
  }

  delete(id: number): Observable<any> {
    return this.httpClient.delete(`/api/cultural-offerings/${id}`);
  }

  private queryLocation(address: string): string {
    return `https://nominatim.openstreetmap.org/search?format=jsonv2&q=${address}&limit=4`;
  }

  private reverseGeocoding(latLng: any[]): string {
    return `https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${latLng[0]}&lon=${latLng[1]}`;
  }

}
