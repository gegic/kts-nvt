import * as L from 'leaflet';
import {CulturalOffering} from './cultural-offering';
import {BehaviorSubject} from 'rxjs';

export class CulturalOfferingMarker extends L.Marker {
  culturalOffering: CulturalOffering;
  private visible = true;
  hovering: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(culturalOffering: CulturalOffering) {
    super([culturalOffering.latitude ?? 0, culturalOffering.longitude ?? 0]);
    this.culturalOffering = culturalOffering;
  }

  setVisible(val: boolean): void {
    this.visible = val;
    if (val) {
      this.setOpacity(1);
    } else {
      this.setOpacity(0);
    }
  }

  isVisible(): boolean {
    return this.visible;
  }
}
