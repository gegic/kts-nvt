import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { CulturalOfferingAboutComponent } from './cultural-offering-about.component';
import {CulturalOfferingDetailsService} from '../../core/services/cultural-offering-details/cultural-offering-details.service';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {BehaviorSubject} from 'rxjs';
import {By} from '@angular/platform-browser';

describe('CulturalOfferingAboutComponent', () => {
  let component: CulturalOfferingAboutComponent;
  let fixture: ComponentFixture<CulturalOfferingAboutComponent>;
  let detailsService: CulturalOfferingDetailsService;
  let culturalOffering: CulturalOffering;
  beforeEach(async () => {
    culturalOffering = {
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
    const detailsServiceMock = {
      culturalOffering: new BehaviorSubject<CulturalOffering | undefined>(culturalOffering)
    };
    await TestBed.configureTestingModule({
      declarations: [ CulturalOfferingAboutComponent ],
      providers: [
        {provide: CulturalOfferingDetailsService, useValue: detailsServiceMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    detailsService = TestBed.inject(CulturalOfferingDetailsService);
    fixture = TestBed.createComponent(CulturalOfferingAboutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set the map and the marker up', fakeAsync(() => {

    tick();
    fixture.detectChanges();

    expect(component.mapElement.nativeElement.children.length).toBeGreaterThan(0);
  }));

  it('should not set address if there is not address in the culturalOffering', fakeAsync(() => {
    culturalOffering.address = '';
    detailsService.culturalOffering.next(culturalOffering);

    tick();
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.address'))).toBeFalsy();
  }));

  it('should not set brief info if there is no brief info in the culturalOffering', fakeAsync(() => {
    culturalOffering.briefInfo = '';
    detailsService.culturalOffering.next(culturalOffering);

    tick();
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.brief-info'))).toBeFalsy();
  }));

  it('should not set additional info if there is none in the cultural offering', fakeAsync(() => {
    culturalOffering.additionalInfo = '';
    detailsService.culturalOffering.next(culturalOffering);

    tick();
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.additional-info'))).toBeFalsy();
  }));

  it('should get cultural offering from detailsservice', () => {
    expect(component.culturalOffering).toEqual(culturalOffering);
  });
});
