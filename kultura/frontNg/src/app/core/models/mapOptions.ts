import * as L from 'leaflet';

export class MapOptions implements L.MapOptions {
  zoomControl = false;
  maxBounds = L.latLngBounds(L.latLng(-90, -180), L.latLng(90, 180));
  attributionControl = false;
  maxBoundsViscosity = 1.0;
  minZoom = 3;
}
