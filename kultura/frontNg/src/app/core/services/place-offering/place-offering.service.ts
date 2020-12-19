import { Injectable } from '@angular/core';
import * as L from 'leaflet';
import {BehaviorSubject, Observable, PartialObserver} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Place} from '../../models/place';

declare class ToAddressJson {
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

  setPlace(latLng: L.LatLng): void {
    this.httpClient.get(this.reverseGeocoding(latLng))
      .subscribe(
        data => {
          const jsonv2 = data as ToAddressJson;
          if (!jsonv2.display_name) {
            this.place.next(new Place(null, true));
          } else {
            this.place.next(new Place(jsonv2.display_name, false));
          }
          this.latLng.next(latLng);
        }
      );
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
