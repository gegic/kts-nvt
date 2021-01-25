import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { OfferingsListComponent } from './offerings-list.component';
import {MapService} from '../../core/services/map/map.service';
import {CulturalOfferingDetailsService} from '../../core/services/cultural-offering-details/cultural-offering-details.service';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {CulturalOfferingMarker} from '../../core/models/culturalOfferingMarker';
import {VirtualScrollerModule} from 'primeng/virtualscroller';

describe('OfferingsListComponent', () => {
  let component: OfferingsListComponent;
  let fixture: ComponentFixture<OfferingsListComponent>;
  let mapService: MapService;

  beforeEach(async () => {
    const offerings = getCulturalOfferings();
    const mockMapService = {
      markers: [new CulturalOfferingMarker(offerings[0]), new CulturalOfferingMarker(offerings[1])]
    };
    await TestBed.configureTestingModule({
      declarations: [ OfferingsListComponent ],
      imports: [VirtualScrollerModule],
      providers: [
        {provide: MapService, useValue: mockMapService}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    mapService = TestBed.inject(MapService);
    fixture = TestBed.createComponent(OfferingsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should emit collapse', fakeAsync(() => {
    const spyEmit = spyOn(component.clickCollapse, 'emit');
    component.onClickCollapse();

    tick();
    fixture.detectChanges();

    expect(spyEmit).toHaveBeenCalled();
  }));

  it('should get visible markers', () => {
    expect(component.markers.length).toEqual(2);

    mapService.markers[0].setVisible(false);

    expect(component.markers.length).toEqual(1);

    mapService.markers[1].setVisible(false);

    expect(component.markers.length).toEqual(0);
  });
});

function getCulturalOfferings(): CulturalOffering[] {
  return  [
    {
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
    },
    {
      id: 2,
      latitude: 13.4,
      longitude: 15.1,
      name: 'My second offering',
      briefInfo: 'Some info',
      address: 'Svetozara Miletica',
      numSubscribed: 1,
      subscribed: false,
      subcategoryId: 1,
      categoryId: 1,
      subcategoryName: 'Vasar',
      categoryName: 'Manifestacija',
      overallRating: 0,
      numReviews: 0,
      numPhotos: 0,
      photoId: 2
    }
  ];
}

