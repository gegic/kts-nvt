import { Injectable } from '@angular/core';
import {CulturalOfferingMarker} from '../models/culturalOfferingMarker';
import * as L from 'leaflet';
import {BehaviorSubject} from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class MapPopupService {

  open: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  culturalOfferingMarker?: CulturalOfferingMarker;
  pixelPoint: BehaviorSubject<L.Point | undefined> = new BehaviorSubject<L.Point | undefined>(undefined);
  constructor() { }

  openPopup(map: L.Map | null, marker: CulturalOfferingMarker): void {
    if (!map) {
      return;
    }
    this.culturalOfferingMarker = marker;
    this.pixelPoint.next(map.latLngToContainerPoint(this.culturalOfferingMarker.getLatLng()));
    console.log(this.pixelPoint.getValue());
    this.open.next(true);
  }

  closePopup(): void {
    this.open.next(false);
  }
}
