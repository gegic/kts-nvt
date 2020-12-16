import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import * as L from 'leaflet';
import {MapOptions} from '../../core/models/mapOptions';
import {MapService, ZOOM_IMPORTANT, ZOOM_REGULAR} from '../../core/services/map/map.service';
@Component({
  selector: 'app-map-view',
  templateUrl: './map-view.component.html',
  styleUrls: ['./map-view.component.scss']
})
export class MapViewComponent implements OnInit, AfterViewInit {

  private map: L.Map | null = null;
  private tileLayer: L.TileLayer | null = null;

  @ViewChild('map')
  mapElement: ElementRef<HTMLElement> | null = null;

  constructor(private mapService: MapService) {
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    const mapOptions = new MapOptions();

    if (!this.mapElement) {
      return;
    }
    this.map = L.map(this.mapElement.nativeElement, mapOptions).setView([0, 0], 2);
    const options = {
      maxZoom: 19,
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
      zIndex: 0,
      autoZIndex: false
    };
    this.tileLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', options);
    this.tileLayer.addTo(this.map);

    this.map.whenReady(() => {
      setTimeout(() => {
        this.map?.invalidateSize();
      }, 0);
    });

    this.map.on('zoomend', (event: L.LeafletEvent) => {
      this.mapService.zoom.next(event.target.getZoom());
    });

    this.mapService.zoom.subscribe(val => {
      if (val > ZOOM_REGULAR) {
        console.log('REGULAR LOAD');
      } else if (val > ZOOM_IMPORTANT) {
        console.log('IMPORTANT LOAD');
      }
    });
  }
}
