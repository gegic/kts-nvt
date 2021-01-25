import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { OfferingItemComponent } from './offering-item.component';
import {Router} from '@angular/router';
import Spy = jasmine.Spy;
import {RouterTestingModule} from '@angular/router/testing';
import {CulturalOfferingMarker} from '../../core/models/culturalOfferingMarker';
import {DomEvent} from 'leaflet';
import off = DomEvent.off;
import {CardModule} from 'primeng/card';

describe('OfferingItemComponent', () => {
  let component: OfferingItemComponent;
  let fixture: ComponentFixture<OfferingItemComponent>;
  let spyNavigate: Spy;
  let marker: CulturalOfferingMarker;

  beforeEach(async () => {
    const offering = {
      id: 1,
      latitude: 13,
      longitude: 15,
      name: 'My offering',
      briefInfo: 'Some info',
      address: 'Svetozara Markovica',
      numSubscribed: 0,
      subscribed: false,
      subcategoryId: 1,
      categoryId: 1,
      subcategoryName: 'Vasar',
      categoryName: 'Manifestacija',
      overallRating: 0,
      numReviews: 0,
      numPhotos: 0,
      photoId: 1
    };
    marker = new CulturalOfferingMarker(offering);
    await TestBed.configureTestingModule({
      declarations: [ OfferingItemComponent ],
      imports: [RouterTestingModule, CardModule]
    })
    .compileComponents();
  });

  beforeEach(() => {
    spyNavigate = spyOn(TestBed.inject(Router), 'navigate');
    fixture = TestBed.createComponent(OfferingItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    component.culturalOfferingMarker = marker;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get photo url for marker', () => {
    expect(component.photoUrl).toEqual('/photos/main/thumbnail/1.png');
  });

  it('should get number of subscribed users', () => {
    expect(component.numSubscribers).toEqual(marker.culturalOffering.numSubscribed);
  });

  it('should start to hover', fakeAsync(() => {
    component.hoverStarted();
    tick();
    expect(marker.hovering.getValue()).toBeTrue();
  }));

  it('should end hovering', fakeAsync(() => {
    component.hoverEnded();
    tick();
    expect(marker.hovering.getValue()).toBeFalse();
  }));

  it('should navigate straight to cultural offering', () => {
    component.onClickCard();
    expect(spyNavigate).toHaveBeenCalledWith(['/cultural-offering/1']);
  });

  it('should get name length', () => {
    expect(component.nameLength).toEqual(marker.culturalOffering.name.length);
  });

  it('should get address length', () => {
    expect(component.addressLength).toEqual(marker.culturalOffering.address.length);
  });

  it('should get subcategory name length', () => {
    expect(component.subcategoryNameLength).toEqual(marker.culturalOffering.subcategoryName.length);
  });

});
