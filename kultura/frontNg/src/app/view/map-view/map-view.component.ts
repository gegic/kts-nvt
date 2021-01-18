import {AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import * as L from 'leaflet';
import {MapOptions} from '../../core/models/mapOptions';
import {MapService, ZOOM_IMPORTANT, ZOOM_REGULAR} from '../../core/services/map/map.service';
import {debounceTime, distinctUntilChanged} from 'rxjs/operators';
import {LoadLevel} from '../../core/models/loadLevel';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {CulturalOfferingMarker} from '../../core/models/culturalOfferingMarker';
import {inOutAnimation} from './view-offering-button-animation';
import {Router} from '@angular/router';
import {AuthService} from '../../core/services/auth/auth.service';
import {User} from '../../core/models/user';
import {Subscription} from "rxjs";

@Component({
  selector: 'app-map-view',
  templateUrl: './map-view.component.html',
  styleUrls: ['./map-view.component.scss'],
  animations: [inOutAnimation]
})
export class MapViewComponent implements OnInit, AfterViewInit, OnDestroy {

  private subscriptions: Subscription[] = [];

  viewOfferings = false;
  private map: L.Map | null = null;
  private tileLayer: L.TileLayer | null = null;

  @ViewChild('map')
  mapElement!: ElementRef<HTMLElement>;

  constructor(private mapService: MapService,
              private router: Router,
              private authService: AuthService) {
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
    this.map = L.map(this.mapElement.nativeElement, mapOptions).setView([0, 0], 2);
    this.map.locate();
    this.onLocate();
    const options = {
      maxZoom: 19,
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
      zIndex: 0,
      autoZIndex: false,
      keepBuffer: 4
    };
    this.tileLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', options);
    this.tileLayer.addTo(this.map);

    this.map.whenReady(() => {
      setTimeout(() => {
        this.map?.invalidateSize();
      }, 0);
    });

    this.loadRegular(this.map);

    this.onZoomLoad();
    this.onMove();
    this.map.on('click', event => {
      console.log(event);
    });

    this.subscriptions.push(
      this.mapService.zoom
        .pipe(debounceTime(200), distinctUntilChanged())
        .subscribe(val => {
        if (val >= ZOOM_REGULAR) {
          this.loadRegular(this.map);
        }
        else {
          this.mapService.removeMarkers();
          this.onClickCollapse();
        }
      })
    );
  }

  private loadRegular(map: L.Map | null): void {
    if (!map){
      return;
    }
    if (map.getZoom() < ZOOM_REGULAR) {
      return;
    }
    const bounds = map.getBounds();
    const northEast = bounds.getNorthEast();
    const southWest = bounds.getSouthWest();
    const latitudeStart = southWest.lat - 0.02;
    const longitudeStart = southWest.lng - 0.02;
    const latitudeEnd = northEast.lat + 0.02;
    const longitudeEnd = northEast.lng + 0.02;
    this.mapService.removeOutOfBounds(latitudeStart, latitudeEnd, longitudeStart, longitudeEnd);
    this.subscriptions.push(
      this.mapService.loadMarkers(latitudeStart, latitudeEnd, longitudeStart, longitudeEnd).subscribe(
        (data: CulturalOffering[]) => {
          const markers = data.map(co => new CulturalOfferingMarker(co));
          markers.forEach(m => {
            if (this.mapService.markers.hasOwnProperty(m.culturalOffering.id ?? 0)) {
              this.mapService.markers[m.culturalOffering.id ?? 0].setVisible(true);
              return;
            }
            this.subscriptions.push(
              m.hovering.subscribe(val => {
                if (val) {
                  m.openPopup();
                } else {
                  m.closePopup();
                }
              })
            );
            m.bindPopup(m.culturalOffering.name ?? '');
            // @ts-ignore
            m.on('mouseover', ev => {
              if (!ev.target.isVisible()) {
                return;
              }
              ev.target.hovering.next(true);
              ev.target.openPopup();
            });
            m.on('mouseout', ev => {
              ev.target.hovering.next(false);
              ev.target.closePopup();
            });
            m.on('click', ev => {
              this.router.navigate([`/cultural-offering/${ev.target?.culturalOffering.id}`]);
            });
            this.mapService.markers[m.culturalOffering.id ?? 0] = m;
            m.addTo(map);
          });
        }
      )
    );
  }

  onClickViewOfferings(): void {
    this.viewOfferings = true;
  }

  onClickCollapse(): void {
    this.viewOfferings = false;
  }

  private onZoomLoad(): void {
    this.map?.on('zoomend', ev => {
      this.mapService.zoom.next(ev.target.getZoom());
    });
  }

  private onMove(): void {
    this.map?.on('moveend', ev => {
      if (this.mapService.zoom.getValue() >= ZOOM_REGULAR) {
        this.loadRegular(ev.target);
      }
    });
  }

  private onLocate(): void {
    this.map?.on('locationfound', ev => {
      this.map?.setView(ev.latlng, 8, {animate: false});
    });
  }

  getUserRole(): string {
    return this.authService.getUserRole();
  }

  get showRegularOfferings(): boolean {
    return this.mapService.zoom.getValue() >= ZOOM_REGULAR;
  }

  ngOnDestroy(): void {
    this.mapService.clearMarkers();
    this.subscriptions.forEach(s => s.unsubscribe());
  }
}
