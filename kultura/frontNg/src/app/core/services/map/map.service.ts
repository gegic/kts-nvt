
import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {CulturalOfferingMarker} from '../../models/culturalOfferingMarker';
import {LoadLevel} from '../../models/loadLevel';
import {HttpClient} from '@angular/common/http';

export const ZOOM_IMPORTANT = 10;
export const ZOOM_REGULAR = 15;

export interface Dictionary<T> {
  [Key: string]: T;
}


@Injectable({
  providedIn: 'root'
})
export class MapService {

  markers: Dictionary<CulturalOfferingMarker> = {};
  zoom = new BehaviorSubject<number>(0);
  loadLevel = new BehaviorSubject<LoadLevel>(LoadLevel.NONE);

  constructor(private httpClient: HttpClient) {}

  loadMarkers(latitudeStart: number,
              latitudeEnd: number,
              longitudeStart: number,
              longitudeEnd: number): Observable<any> {

    return this.httpClient.get(`/api/cultural-offerings/bounds?lng-start=${longitudeStart}&lng-end=${longitudeEnd}&` +
      `lat-start=${latitudeStart}&lat-end=${latitudeEnd}`);
  }

  removeMarkers(): void {
    Object.keys(this.markers).forEach(key => this.markers[key].setVisible(false));
  }

  constructor() {
  }
  removeOutOfBounds(latitudeStart: number,
                    latitudeEnd: number,
                    longitudeStart: number,
                    longitudeEnd: number): void {
    Object.keys(this.markers).forEach(key => {
      const latitude = this.markers[key].culturalOffering.latitude ?? 0;
      const longitude = this.markers[key].culturalOffering.longitude ?? 0;
      if (latitude < latitudeStart || latitude > latitudeEnd ||
        longitude < longitudeStart || longitude > longitudeEnd) {
        this.markers[key].setVisible(false);
      }
    })
  }
}
