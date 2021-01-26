import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { PhotosComponent } from './photos.component';
import {PostsService} from '../../core/services/posts/posts.service';
import {CulturalOfferingDetailsService} from '../../core/services/cultural-offering-details/cultural-offering-details.service';
import {AuthService} from '../../core/services/auth/auth.service';
import {ConfirmationService, MessageService} from 'primeng/api';
import {DialogService} from 'primeng/dynamicdialog';
import {Router} from '@angular/router';
import {PhotoService} from '../../core/services/photos/photo.service';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {User} from '../../core/models/user';
import {Authority} from '../../core/models/authority';
import {Post} from '../../core/models/post';
import * as moment from 'moment/moment';
import {BehaviorSubject, of} from 'rxjs';
import createSpy = jasmine.createSpy;
import {CulturalOfferingPhoto} from '../../core/models/culturalOfferingPhoto';
import {RouterTestingModule} from '@angular/router/testing';
import Spy = jasmine.Spy;
import {FileUpload, FileUploadModule} from 'primeng/fileupload';
import {DialogModule} from 'primeng/dialog';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {GalleriaModule} from 'primeng/galleria';

describe('PhotosComponent', () => {
  let component: PhotosComponent;
  let fixture: ComponentFixture<PhotosComponent>;
  let photoService: PhotoService;
  let dialogService: DialogService;
  let detailsService: CulturalOfferingDetailsService;
  let authService: AuthService;
  let messageService: MessageService;
  let confirmationService: ConfirmationService;
  let addSpy: Spy;
  let router: Router;
  let navigateSpy: Spy;

  beforeEach(async () => {
    const culturalOffering = getCulturalOfferings()[0];
    const detailsServiceMock = {
      culturalOffering: new BehaviorSubject<CulturalOffering | undefined>(culturalOffering)
    };
    const photos = getPhotos();
    const authServiceMock = {
      getUserRole: createSpy('getUserRole').and.returnValue('USER'),
    };
    const photoServiceMock = {
      photos: [],
      getPhotos: createSpy('getPhotos').and.returnValue(of({content: photos, pageable: {pageNumber: 0},
        totalPages: 1})),
      addPhoto: createSpy('addPhoto').and.returnValue(of(null)),
      delete: createSpy('delete').and.returnValue(of(null))
    };
    await TestBed.configureTestingModule({
      declarations: [ PhotosComponent ],
      imports: [RouterTestingModule, HttpClientTestingModule, GalleriaModule, FileUploadModule, DialogModule],
      providers: [
        MessageService,
        ConfirmationService,
        DialogService,
        {provide: CulturalOfferingDetailsService, useValue: detailsServiceMock},
        {provide: AuthService, useValue: authServiceMock},
        {provide: PhotoService, useValue: photoServiceMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    detailsService = TestBed.inject(CulturalOfferingDetailsService);
    photoService = TestBed.inject(PhotoService);
    authService = TestBed.inject(AuthService);
    messageService = TestBed.inject(MessageService);
    confirmationService = TestBed.inject(ConfirmationService);
    dialogService = TestBed.inject(DialogService);
    router = TestBed.inject(Router);
    navigateSpy = spyOn(router, 'navigate');
    addSpy = spyOn(messageService, 'add');
    fixture = TestBed.createComponent(PhotosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not reset photos page if there\'s no cultural offering', () => {
    const spyReset = spyOn(component, 'resetPhotos');

    detailsService.culturalOffering.next(undefined);

    component.ngOnInit();

    expect(spyReset).not.toHaveBeenCalled();
  });

  it('should reset photos if there is a cultural offering', () => {
    const spyReset = spyOn(component, 'resetPhotos');

    component.ngOnInit();

    expect(spyReset).toHaveBeenCalled();
  });

  it('should get photos if there\'s a cultural offering', fakeAsync(() => {
    component.getPhotos();

    tick();
    fixture.detectChanges();

    expect(photoService.getPhotos).toHaveBeenCalledWith(1, 0);
    expect(photoService.photos).toEqual(getPhotos());
    expect(component.page).toEqual(0);
    expect(component.totalPages).toEqual(1);
    expect(component.isPhotosLoading).toBeFalse();
  }));

  it('should get photos if there is not a cultural offering', fakeAsync(() => {
    component.getPhotos();

    tick();
    fixture.detectChanges();

    expect(photoService.getPhotos).toHaveBeenCalledWith(1, 0);
    expect(photoService.photos).toEqual(getPhotos());
    expect(component.page).toEqual(0);
    expect(component.totalPages).toEqual(1);
    expect(component.isPhotosLoading).toBeFalse();
  }));

  it('should get photos on scroll down', () => {
    const getPhotosSpy = spyOn(component, 'getPhotos');

    component.onScrollDown();

    expect(getPhotosSpy).toHaveBeenCalled();
  });

  it('should return thumbnail url for a photo', () => {
    const url = component.getThumbnailUrl(getPhotos()[0]);

    expect(url).toEqual('/photos/thumbnail/1.png');
  });

  it('should return photo url for a photo', () => {
    const url = component.getPhotoUrl(getPhotos()[0]);

    expect(url).toEqual('/photos/1.png');
  });

  it('should clear files from upload input', fakeAsync(() => {
    component.clearFiles();

    tick();
    fixture.detectChanges();

    expect((component.upload as FileUpload).hasFiles()).toBeFalse();
  }));

  it('should upload photos', fakeAsync(() => {
    const resetSpy = spyOn(component, 'resetPhotos');
    const clearSpy = spyOn(component, 'clearFiles');

    const blob = new Blob([''], { type: 'text/html' });
    blob[`lastModifiedDate`] = '';
    blob[`name`] = 'photo';
    const fakeFile = blob as File;


    const event = {
      files: [fakeFile]
    };
    component.onClickUpload(event);

    tick();
    fixture.detectChanges();

    expect(photoService.addPhoto).toHaveBeenCalledWith(jasmine.any(Blob), 1);
    expect(component.isOpenAddDialog).toBeFalse();
    expect(addSpy).toHaveBeenCalled();
    expect(component.uploadLoading).toBeFalse();
    expect(resetSpy).toHaveBeenCalled();
    expect(clearSpy).toHaveBeenCalled();

  }));

  it('should reset photos', fakeAsync(() => {
    const getSpy = spyOn(component, 'getPhotos');
    component.resetPhotos();

    expect(component.isPhotosLoading).toBeTrue();
    expect(photoService.photos.length).toEqual(0);
    expect(component.page).toEqual(-1);
    expect(component.totalPages).toEqual(0);
    expect(getSpy).toHaveBeenCalled();
  }));

  it('should get user role from authService', () => {
    const role = component.getUserRole();

    expect(role).toEqual('USER');
  });

  it('should set photo hover on true', () => {
    const photo = getPhotos()[0];
    component.onPhotoHoverStart(photo);

    expect(photo.hovering).toBeTrue();
  });

  it('should set photo hover on false', () => {
    const photo = getPhotos()[0];
    component.onPhotoHoverEnd(photo);

    expect(photo.hovering).toBeFalse();
  });

  it('should open confirm dialog to delete photo', fakeAsync(() => {
    const confirmSpy = spyOn(confirmationService, 'confirm');

    component.activeIndex = 0;
    component.onClickDelete();

    tick();
    fixture.detectChanges();

    expect(confirmSpy).toHaveBeenCalled();
  }));

  it('should confirm photo deletion', fakeAsync(() => {
    const resetSpy = spyOn(component, 'resetPhotos');
    component.photoDeletionConfirmed(getPhotos()[0]);

    tick();
    fixture.detectChanges();

    expect(photoService.delete).toHaveBeenCalledWith(1);
    expect(addSpy).toHaveBeenCalled();
    expect(component.galleriaVisible).toBeFalse();
    expect(resetSpy).toHaveBeenCalled();
  }));

  it('should set active index', fakeAsync(() => {
    component.page = 0;
    component.totalPages = 1;

    component.imageClick(0);

    expect(component.activeIndex).toEqual(0);
    expect(component.galleriaVisible).toBeTrue();
  }));

  it('should get photos from photoservice', () => {
    expect(component.photos).toEqual(getPhotos());
  });

  it('should remove photos', () => {
    component.ngOnDestroy();

    expect(photoService.photos.length).toEqual(0);
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

function getPhotos(): CulturalOfferingPhoto[] {
  return [
    {
      id: 1,
      culturalOfferingId: 1,
      hovering: false
    },
    {
      id: 2,
      culturalOfferingId: 1,
      hovering: false
    }
  ];
}
