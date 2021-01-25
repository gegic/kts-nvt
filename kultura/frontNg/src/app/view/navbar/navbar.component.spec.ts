import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { NavbarComponent } from './navbar.component';
import {AuthService} from '../../core/services/auth/auth.service';
import createSpy = jasmine.createSpy;
import {User} from '../../core/models/user';
import {Authority} from '../../core/models/authority';
import {BehaviorSubject} from 'rxjs';
import {CulturalOfferingsService} from '../../core/services/cultural-offerings/cultural-offerings.service';
import {RouterTestingModule} from '@angular/router/testing';
import {MenuModule} from 'primeng/menu';
import {AvatarModule} from 'ngx-avatar';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import Spy = jasmine.Spy;
import {Router} from '@angular/router';
import {ensureOriginalSegmentLinks} from '@angular/compiler-cli/src/ngtsc/sourcemaps/src/source_file';

describe('NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;
  let authService: AuthService;
  let culturalOfferingsService: CulturalOfferingsService;
  let router: Router;
  let spyNavigate: Spy;

  beforeEach(async () => {
    const authServiceMock = {
      logout: createSpy('logout'),
      isLoggedIn: createSpy('isLoggedIn').and.returnValue(true),
      getUserRole: createSpy('getUserRole').and.returnValue('USER'),
      user: new BehaviorSubject<User | null>(getUser())
    };

    const culturalOfferingServiceMock = {
      searchQuery: new BehaviorSubject<string>('')
    };
    await TestBed.configureTestingModule({
      declarations: [ NavbarComponent ],
      imports: [
        RouterTestingModule.withRoutes([{path: '', component: NavbarComponent}, {path: 'nesto', component: NavbarComponent}]),
        MenuModule,
        AvatarModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientTestingModule
      ],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        {provide: CulturalOfferingsService, useValue: culturalOfferingServiceMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    authService = TestBed.inject(AuthService);
    culturalOfferingsService = TestBed.inject(CulturalOfferingsService);
    router = TestBed.inject(Router);
    spyNavigate = spyOn(router, 'navigate');

    fixture = TestBed.createComponent(NavbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should subscribe to searchquery', fakeAsync(() => {
    culturalOfferingsService.searchQuery.next('vas');
    tick();
    expect(component.searchQuery).toEqual('vas');
    culturalOfferingsService.searchQuery.next('nesto');
    tick();
    expect(component.searchQuery).toEqual('nesto');
  }));

  it('should logout', () => {
    component.onClickLogout();
    expect(authService.logout).toHaveBeenCalled();
  });

  it('should set search query', () => {
    component.searchQuery = 'NESTO';
    component.setSearchQuery({key: 'Enter'});

    expect(culturalOfferingsService.searchQuery.getValue()).toEqual('NESTO');
    expect(spyNavigate).toHaveBeenCalledWith(['list-view']);
  });

  it('should navigate to login', () => {
    component.onClickSignIn();
    expect(spyNavigate).toHaveBeenCalledWith(['login']);
  });

  it('should check if user is logged in', () => {
    expect(component.isLoggedIn()).toBeTrue();
  });

  it('should get user role from authservice', () => {
    expect(component.getUserRole()).toEqual('USER');
  });

  it('should get full name', () => {
    expect(component.name).toEqual('Korisnik Korisnikovic');
  });

  it('should check if link is active', fakeAsync(() => {
    router.navigateByUrl('/');
    tick();

    expect(component.isLinkActive('')).toBeTrue();

    router.navigateByUrl('/nesto?drugo=123');
    tick();

    expect(component.isLinkActive('nesto')).toBeTrue();

  }));
});

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
