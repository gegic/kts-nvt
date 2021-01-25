import {AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import * as L from 'leaflet';
import {MapOptions} from '../../core/models/mapOptions';
import {CulturalOfferingDetailsService} from '../../core/services/cultural-offering-details/cultural-offering-details.service';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-cultural-offering-about',
  templateUrl: './cultural-offering-about.component.html',
  styleUrls: ['./cultural-offering-about.component.scss']
})
export class CulturalOfferingAboutComponent implements AfterViewInit, OnDestroy {

  private subscriptions: Subscription[] = [];

  private map?: L.Map;
  private tileLayer?: L.TileLayer;
  private marker?: L.Marker;
  @ViewChild('map')
  mapElement!: ElementRef<HTMLElement>;

  constructor(private detailsService: CulturalOfferingDetailsService) { }

  ngAfterViewInit(): void {
    L.Icon.Default.mergeOptions({
      iconRetinaUrl: 'assets/marker-icon-2x.png',
      iconUrl: 'assets/marker-icon.png',
      shadowUrl: 'assets/marker-shadow.png'
    });

    const mapOptions = new MapOptions();
    mapOptions.touchZoom = false;
    mapOptions.doubleClickZoom = false;
    mapOptions.scrollWheelZoom = false;
    mapOptions.boxZoom = false;
    mapOptions.keyboard = false;
    mapOptions.dragging = false;

    this.map = L.map(this.mapElement.nativeElement, mapOptions).setView([0, 0], 2);

    const options = {
      maxZoom: 19,
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
      zIndex: 0,
      autoZIndex: false,
      keepBuffer: 4
    };
    this.tileLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', options);

    this.tileLayer.addTo(this.map);

    this.subscriptions.push(
      this.detailsService.culturalOffering.subscribe(val => {
        if (this.marker) {
          if (this.map) {
            this.marker.removeFrom(this.map);
          }
          this.marker = undefined;
        }
        if (!val) {
          return;
        }
        const latLng = new L.LatLng(val?.latitude ?? 0, val?.longitude ?? 0);
        this.marker = new L.Marker(latLng);
        if (this.map) {
          this.marker.addTo(this.map);
          this.map.setView(latLng, 15);
        }
      })
    );

  }

  get culturalOffering(): CulturalOffering | undefined {
    return this.detailsService.culturalOffering.getValue();
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }
}
