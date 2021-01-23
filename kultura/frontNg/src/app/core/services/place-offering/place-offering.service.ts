import {Injectable} from '@angular/core';
import * as L from 'leaflet';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Place} from '../../models/place';

export declare class ToAddressJson {
  'display_name': string;
}

export declare class NominatimPlace {
  'place_id': number;
  'display_name': string;
  lat: number;
  lon: number;
}

@Injectable({
  providedIn: 'root'
})
export class PlaceOfferingService {


  place: BehaviorSubject<Place> =
    new BehaviorSubject<Place>(new Place(null, false));

  latLng: BehaviorSubject<L.LatLng | null> = new BehaviorSubject<L.LatLng | null>(null);

  constructor(private httpClient: HttpClient) {
  }

  getAddress(latLng: L.LatLng): Observable<any> {
    return this.httpClient.get(this.reverseGeocoding(latLng));
  }

  getRecommendations(address: string): Observable<any> {
    return this.httpClient.get(this.queryLocation(address));
  }

  recommendationSelected(address: string, latLng: [number, number]): void {
    this.latLng.next(new L.LatLng(latLng[0], latLng[1]));
    this.place.next(new Place(address, false));
  }

  reset(): void {
    this.place.next(new Place(null, false));
    this.latLng.next(null);
  }

  private reverseGeocoding(latLng: L.LatLng): string {
    return `https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${latLng.lat}&lon=${latLng.lng}`;
  }

  private queryLocation(address: string): string {
    return `https://nominatim.openstreetmap.org/search?format=jsonv2&q=${address}&limit=6`;
  }

}
