import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Dictionary, MapService} from '../../core/services/map/map.service';
import {CulturalOfferingMarker} from '../../core/models/culturalOfferingMarker';

@Component({
  selector: 'app-offerings-list',
  templateUrl: './offerings-list.component.html',
  styleUrls: ['./offerings-list.component.scss']
})
export class OfferingsListComponent {

  @Output()
  clickCollapse: EventEmitter<any> = new EventEmitter<undefined>();

  constructor(private mapService: MapService) { }

  onClickCollapse(): void {
    this.clickCollapse.emit();
  }

  get markers(): CulturalOfferingMarker[] {
    return Object.keys(this.mapService.markers)
      .filter(k => (this.mapService.markers[k] as CulturalOfferingMarker).isVisible())
      .map(k => this.mapService.markers[k]);
  }

}
