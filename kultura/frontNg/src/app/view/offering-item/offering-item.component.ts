import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {CulturalOfferingMarker} from '../../core/models/culturalOfferingMarker';

@Component({
  selector: 'app-offering-item',
  templateUrl: './offering-item.component.html',
  styleUrls: ['./offering-item.component.scss']
})
export class OfferingItemComponent implements OnInit {

  @Input()
  culturalOfferingMarker?: CulturalOfferingMarker;

  @ViewChild('card')
  card?: ElementRef;

  constructor() {}

  ngOnInit(): void {
  }

  get photoUrl(): string {
    return `/photos/main/thumbnail/${this.culturalOfferingMarker?.culturalOffering.photoId ?? -1}.png`;
  }

  get numSubscribers(): number {
    return this.culturalOfferingMarker?.culturalOffering?.numSubscribers ?? 0;
  }

  hoverStarted(): void {
    this.culturalOfferingMarker?.hovering.next(true);
  }

  hoverEnded(): void {
    this.culturalOfferingMarker?.hovering.next(false);
  }

  get hovering(): boolean {
    return this.culturalOfferingMarker?.hovering.getValue() ?? false;
  }
}
