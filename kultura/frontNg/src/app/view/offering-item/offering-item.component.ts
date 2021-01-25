import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {CulturalOfferingMarker} from '../../core/models/culturalOfferingMarker';
import {Router} from '@angular/router';

@Component({
  selector: 'app-offering-item',
  templateUrl: './offering-item.component.html',
  styleUrls: ['./offering-item.component.scss']
})
export class OfferingItemComponent {

  @Input()
  culturalOfferingMarker?: CulturalOfferingMarker;

  @ViewChild('card')
  card?: ElementRef;

  constructor(private router: Router) {}

  get photoUrl(): string {
    return `/photos/main/thumbnail/${this.culturalOfferingMarker?.culturalOffering.photoId ?? -1}.png`;
  }

  get numSubscribers(): number {
    return this.culturalOfferingMarker?.culturalOffering?.numSubscribed ?? 0;
  }

  hoverStarted(): void {
    this.culturalOfferingMarker?.hovering.next(true);
  }

  hoverEnded(): void {
    this.culturalOfferingMarker?.hovering.next(false);
  }

  onClickCard(): void {
    this.router.navigate([`/cultural-offering/${this.culturalOfferingMarker?.culturalOffering.id}`]);
  }

  get hovering(): boolean {
    return this.culturalOfferingMarker?.hovering.getValue() ?? false;
  }

  get nameLength(): number {
    return this.culturalOfferingMarker?.culturalOffering.name?.length ?? 0;
  }

  get addressLength(): number {
    return this.culturalOfferingMarker?.culturalOffering.address?.length ?? 0;
  }

  get subcategoryNameLength(): number {
    return this.culturalOfferingMarker?.culturalOffering.subcategoryName?.length ?? 0;
  }
}
