import { Component, OnInit } from '@angular/core';
import {MapPopupService} from '../../core/services/map-popup.service';

@Component({
  selector: 'app-map-popup',
  templateUrl: './map-popup.component.html',
  styleUrls: ['./map-popup.component.scss']
})
export class MapPopupComponent implements OnInit {

  style = {
    position: 'relative',
    top: '0',
    left: '0',
    height: '8vh',
  };

  constructor(private popupService: MapPopupService) { }

  ngOnInit(): void {
    this.popupService.pixelPoint.subscribe(pos => {
      this.style.top = pos?.y + 'px';
      this.style.left = ((pos?.x ?? 0) + 10) + 'px';
    });
  }

  get name(): string {
    return this.popupService.culturalOfferingMarker?.culturalOffering.name ?? '';
  }

  get visible(): boolean {
    return this.popupService.open.getValue();
  }

}
