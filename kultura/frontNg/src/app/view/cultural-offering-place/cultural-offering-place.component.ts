import {AfterViewInit, Component, ElementRef, EventEmitter, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import * as L from 'leaflet';
import {MapOptions} from '../../core/models/mapOptions';
import {PlaceOfferingService} from '../../core/services/place-offering/place-offering.service';
import {DynamicDialogRef} from 'primeng/dynamicdialog';
import {MessageService} from 'primeng/api';

declare function require(name: string): any;

@Component({
  selector: 'app-cultural-offering-place',
  templateUrl: './cultural-offering-place.component.html',
  styleUrls: ['./cultural-offering-place.component.scss']
})
export class CulturalOfferingPlaceComponent implements OnInit, AfterViewInit, OnDestroy {

  private map: L.Map | null = null;
  private tileLayer: L.TileLayer | null = null;
  private currentMarker: L.Marker | null = null;

  @ViewChild('map')
  mapElement: ElementRef<HTMLElement> | null = null;

  constructor(private placeOfferingService: PlaceOfferingService,
              private ref: DynamicDialogRef,
              private messageService: MessageService) {
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    L.Icon.Default.mergeOptions({
      iconRetinaUrl: 'assets/marker-icon-2x.png',
      iconUrl: 'assets/marker-icon.png',
      shadowUrl: 'assets/marker-shadow.png'
    });


    const mapOptions = new MapOptions();

    if (!this.mapElement) {
      return;
    }
    this.map = new L.Map(this.mapElement.nativeElement, mapOptions).setView([0, 0], 2);
    this.map.locate();
    this.onLocate();
    const options = {
      maxZoom: 19,
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
      zIndex: 0,
      autoZIndex: false
    };
    this.tileLayer = new L.TileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', options);
    this.tileLayer.addTo(this.map);

    if (!!this.latLng) {
      this.setCoordinates(this.latLng);
      this.map.setView(this.latLng, this.map.getZoom());
    }

    this.map.whenReady(() => {
      setTimeout(() => {
        this.map?.invalidateSize();
      }, 0);
    });

    this.map.addEventListener('click', ev => this.addMarker(ev));
  }

  addMarker(event: any): void {
    const latLng = event.latlng;
    if (this.currentMarker !== null) {
      this.map?.removeLayer(this.currentMarker);
    }
    this.setCoordinates(latLng);
  }

  private setCoordinates(latLng: L.LatLng): void {
    this.placeOfferingService.setPlace(latLng);
    this.currentMarker = new L.Marker(latLng);
    this.map?.addLayer(this.currentMarker);
  }

  onClickPlace(): void {

    if (!this.address) {
      this.messageService.add({
        severity: 'error',
        summary: 'Invalid address',
        detail: 'Please select a valid location.'
      });
      return;
    }

    this.ref.close({
      address: this.address,
      coordinates: [this.latLng?.lat, this.latLng?.lng]
    });
  }

  get address(): string {
    return this.placeOfferingService.place.getValue().address ?? '';
  }

  get quickAddress(): string {
    return this.placeOfferingService.place.getValue().address ?? 'Invalid address';
  }

  get latLng(): L.LatLng | null {
    return this.placeOfferingService.latLng.getValue();
  }

  private onLocate(): void {
    this.map?.on('locationfound', ev => {
      this.map?.setView(ev.latlng, 8, {animate: false});
    });
  }

  ngOnDestroy(): void {
    this.currentMarker?.remove();
    this.map?.remove();
    this.tileLayer?.remove();
  }
}
