import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { CulturalOfferingDetailsComponent } from './cultural-offering-details.component';
import {CulturalOfferingDetailsService} from '../../core/services/cultural-offering-details/cultural-offering-details.service';
import createSpy = jasmine.createSpy;
import {CulturalOffering} from '../../core/models/cultural-offering';
import {BehaviorSubject, of} from 'rxjs';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from '../../core/models/user';
import {Authority} from '../../core/models/authority';
import {AuthService} from '../../core/services/auth/auth.service';
import {CulturalOfferingsService} from '../../core/services/cultural-offerings/cultural-offerings.service';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {Confirmation, ConfirmationService, MessageService} from 'primeng/api';
import {Title} from '@angular/platform-browser';
import {DetailsNavigationComponent} from '../details-navigation/details-navigation.component';
import {ButtonModule} from 'primeng/button';
import {SkeletonModule} from 'primeng/skeleton';

describe('CulturalOfferingDetailsComponent', () => {
  let component: CulturalOfferingDetailsComponent;
  let fixture: ComponentFixture<CulturalOfferingDetailsComponent>;
  let detailsService: CulturalOfferingDetailsService;
  let activatedRoute: ActivatedRoute;
  let authService: AuthService;
  let culturalOfferingsService: CulturalOfferingsService;
  let router: Router;
  let messageService: MessageService;
  let confirmationService: ConfirmationService;
  let titleService: Title;
  let navigateSpy: any;
  let confirmSpy: any;
  let addSpy: any;

  beforeEach(async () => {
    const culturalOffering = getCulturalOfferings()[0];
    const subscribedCulturalOffering = {...culturalOffering, subscribed: true};
    const detailsServiceMock = {
      getCulturalOffering: createSpy('getCulturalOffering').and.returnValue(of(culturalOffering)),
      culturalOffering: new BehaviorSubject<CulturalOffering | undefined>(undefined)
    };
    const activatedRouteMock = {params: of({id: 1})};

    const user = getUser();
    const authServiceMock = {
      getUserRole: createSpy('getUserRole').and.returnValue(user.getRole()),
      user: new BehaviorSubject<User | null>(user),
      isLoggedIn: createSpy('isLoggedIn').and.returnValue(true)
    };
    const culturalOfferingsServiceMock = {
      subscribe: createSpy('subscribe').and.returnValue(of(subscribedCulturalOffering)),
      unsubscribe: createSpy('unsubscribe').and.returnValue(of(culturalOffering)),
      delete: createSpy('delete').and.returnValue(of(null))
    };
    const titleMock = {
      setTitle: createSpy('setTitle')
    };
    await TestBed.configureTestingModule({
      declarations: [ CulturalOfferingDetailsComponent, DetailsNavigationComponent ],
      imports: [RouterTestingModule, HttpClientTestingModule, ButtonModule, SkeletonModule],
      providers: [
        {provide: CulturalOfferingDetailsService, useValue: detailsServiceMock},
        {provide: ActivatedRoute, useValue: activatedRouteMock},
        {provide: AuthService, useValue: authServiceMock},
        {provide: CulturalOfferingsService, useValue: culturalOfferingsServiceMock},
        {provide: Title, useValue: titleMock},
        MessageService,
        ConfirmationService
      ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    detailsService = TestBed.inject(CulturalOfferingDetailsService);
    activatedRoute = TestBed.inject(ActivatedRoute);
    authService = TestBed.inject(AuthService);
    culturalOfferingsService = TestBed.inject(CulturalOfferingsService);
    router = TestBed.inject(Router);
    titleService = TestBed.inject(Title);
    messageService = TestBed.inject(MessageService);
    confirmationService = TestBed.inject(ConfirmationService);
    navigateSpy = spyOn(router, 'navigate');
    confirmSpy = spyOn(confirmationService, 'confirm').and.callFake((conf: Confirmation) => {
      return conf.accept();
    });
    addSpy = spyOn(messageService, 'add');
    fixture = TestBed.createComponent(CulturalOfferingDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect to homepage if no id', () => {
    (component as any).activatedRoute = {params: of({})};

    component.ngOnInit();

    expect(navigateSpy).toHaveBeenCalledWith(['']);
  });

  it('should get cultural offering if there is id', fakeAsync(() => {
    component.ngOnInit();
    tick();
    fixture.detectChanges();

    expect(detailsService.getCulturalOffering).toHaveBeenCalledWith(1, 1);
    expect(titleService.setTitle).toHaveBeenCalledWith('My offering | kultura');
    expect(component.isOfferingLoading).toBeFalse();
  }));

  it('should get user role from authService', () => {
    expect(component.getUserRole()).toEqual('USER');
    expect(authService.getUserRole).toHaveBeenCalled();
  });

  it('should get cultural offering for the given id', fakeAsync(() => {
    component.getCulturalOffering(1);

    tick();
    fixture.detectChanges();

    expect(detailsService.getCulturalOffering).toHaveBeenCalledWith(1, 1);
    expect(titleService.setTitle).toHaveBeenCalledWith('My offering | kultura');
    expect(component.isOfferingLoading).toBeFalse();
  }));

  it('should call confirmationservice confirm', () => {
    component.onClickDelete();
    expect(confirmSpy).toHaveBeenCalled();
  });

  it('should navigate to edit cultural offering', () => {
    component.onClickEdit();

    expect(navigateSpy).toHaveBeenCalledWith(['/edit-offering/1']);
  });

  it('should subscribe a user when logged in', fakeAsync(() => {
    component.onClickSubscribe();

    tick();
    fixture.detectChanges();

    expect(culturalOfferingsService.subscribe).toHaveBeenCalledWith(1, 1);
    expect(detailsService.culturalOffering.getValue().subscribed).toBeTrue();
  }));

  it('should redirect a user when logged in', fakeAsync(() => {
    authService.isLoggedIn = createSpy('isLoggedIn').and.returnValue(false);
    component.onClickSubscribe();

    tick();
    fixture.detectChanges();

    expect(navigateSpy).toHaveBeenCalledWith(['login']);
  }));

  it('should unsubscribe a user', fakeAsync(() => {
    component.onClickUnsubscribe();

    tick();
    fixture.detectChanges();

    expect(culturalOfferingsService.unsubscribe).toHaveBeenCalledWith(1, 1);
    expect(detailsService.culturalOffering.getValue().subscribed).toBeFalse();
  }));

  it('should delete on deletionConfirmed', () => {
    component.deletionConfirmed();

    expect(culturalOfferingsService.delete).toHaveBeenCalledWith(1);
    expect(navigateSpy).toHaveBeenCalledWith(['']);
    expect(addSpy).toHaveBeenCalled();
  });

  it('should redirect to map', () => {
    component.onClickViewMap();
    expect(navigateSpy).toHaveBeenCalledWith([''], {queryParams: {lat: 13, lng: 15}});
  });

  it('should return if user is logged in from authService', () => {
    expect(component.isLoggedIn()).toEqual(authService.isLoggedIn());
  });

  it('should return cultural offering from detailservice', () => {
    expect(component.culturalOffering).toEqual(detailsService.culturalOffering.getValue());
  });

  it('should return photo url', () => {
    expect(component.photoUrl).toEqual('/photos/main/thumbnail/1.png');
  });

  it('should return number of reviews as a string', () => {
    expect(component.reviews).toEqual('No reviews so far.');
    const co = detailsService.culturalOffering.getValue();
    co.numReviews = 10;
    co.overallRating = 3.2;
    detailsService.culturalOffering.next(co);
    expect(component.reviews).toEqual('3.2 rating out of 10 reviews.');
  });

  it('should destroy component', fakeAsync(() => {
    component.ngOnDestroy();

    tick();
    fixture.detectChanges();

    expect(titleService.setTitle).toHaveBeenCalledWith('kultura');
    expect(detailsService.culturalOffering.getValue()).toBeUndefined();
  }));
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

function getUser(): User {
  const authUser: Authority = {
    id: 3,
    authority: 'ROLE_USER'
  };
  const user: User = new User();
  user.id = 1;
  user.email = 'email@mail.com';
  user.firstName = 'Korisnik';
  user.lastName = 'Korisnikovic';
  user.verified = true;
  user.authorities = [authUser];

  return user;
}
