import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { PostsComponent } from './posts.component';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {User} from '../../core/models/user';
import {Authority} from '../../core/models/authority';
import {BehaviorSubject, of} from 'rxjs';
import createSpy = jasmine.createSpy;
import {CulturalOfferingDetailsService} from '../../core/services/cultural-offering-details/cultural-offering-details.service';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../../core/services/auth/auth.service';
import {CulturalOfferingsService} from '../../core/services/cultural-offerings/cultural-offerings.service';
import {Title} from '@angular/platform-browser';
import {ConfirmationService, MessageService} from 'primeng/api';
import {DialogService} from 'primeng/dynamicdialog';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {ButtonModule} from 'primeng/button';
import {SkeletonModule} from 'primeng/skeleton';
import {PostsService} from '../../core/services/posts/posts.service';
import {Post} from '../../core/models/post';
import * as moment from 'moment/moment';
import {CardModule} from 'primeng/card';
import {ScrollTopModule} from 'primeng/scrolltop';
import {DialogModule} from 'primeng/dialog';
import {InputTextareaModule} from 'primeng/inputtextarea';
import {InputTextModule} from 'primeng/inputtext';
import {FormsModule} from '@angular/forms';
import {Test} from 'tslint';
import {MenuModule} from 'primeng/menu';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import Spy = jasmine.Spy;

describe('PostsComponent', () => {
  let component: PostsComponent;
  let fixture: ComponentFixture<PostsComponent>;
  let postsService: PostsService;
  let detailsService: CulturalOfferingDetailsService;
  let authService: AuthService;
  let messageService: MessageService;
  let confirmationService: ConfirmationService;
  let dialogService: DialogService;
  let router: Router;
  let addSpy: any;
  let navigateSpy: any;

  beforeEach(async () => {
    const culturalOffering = getCulturalOfferings()[0];
    const detailsServiceMock = {
      culturalOffering: new BehaviorSubject<CulturalOffering | undefined>(culturalOffering)
    };
    const user = getUser();
    const authServiceMock = {
      getUserRole: createSpy('getUserRole').and.returnValue(user.getRole()),
      user: new BehaviorSubject<User | null>(user),
      isLoggedIn: createSpy('isLoggedIn').and.returnValue(true)
    };
    const postsServiceMock = {
      posts: [],
      getPosts: createSpy('getPosts').and.returnValue(of({content: getPosts(), pageable: {pageNumber: 0},
          totalPages: 1})),
      createPost: createSpy('createPost').and.returnValue(of(null)),
      updatePost: createSpy('updatePost').and.returnValue(of(null)),
      deletePost: createSpy('deletePost').and.returnValue(of(null))
    };

    await TestBed.configureTestingModule({
      declarations: [ PostsComponent ],
      imports: [
        RouterTestingModule,
        HttpClientTestingModule, ButtonModule,
        CardModule,
        ScrollTopModule,
        DialogModule,
        SkeletonModule,
        FormsModule,
        InputTextareaModule,
        MenuModule,
        BrowserAnimationsModule
      ],
      providers: [
        MessageService,
        ConfirmationService,
        DialogService,
        {provide: CulturalOfferingDetailsService, useValue: detailsServiceMock},
        {provide: AuthService, useValue: authServiceMock},
        {provide: PostsService, useValue: postsServiceMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    postsService = TestBed.inject(PostsService);
    detailsService = TestBed.inject(CulturalOfferingDetailsService);
    authService = TestBed.inject(AuthService);
    messageService = TestBed.inject(MessageService);
    confirmationService = TestBed.inject(ConfirmationService);
    dialogService = TestBed.inject(DialogService);
    router = TestBed.inject(Router);
    navigateSpy = spyOn(router, 'navigate');
    addSpy = spyOn(messageService, 'add');

    fixture = TestBed.createComponent(PostsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect to homepage if no cultural offering', () => {
    detailsService.culturalOffering.next(undefined);

    component.ngOnInit();

    expect(navigateSpy).toHaveBeenCalledWith(['']);
  });

  it('should reset posts', fakeAsync(() => {
    component.ngOnInit();

    tick();

    fixture.detectChanges();

    expect(postsService.getPosts).toHaveBeenCalledWith(1, 0);
    expect(postsService.posts.length).toEqual(2);
    expect(component.page).toEqual(0);
    expect(component.totalPages).toEqual(1);
    expect(component.isPostsLoading).toBeFalse();
  }));

  it('should get posts on scroll', fakeAsync(() => {
    component.onScrollDown();

    tick();

    fixture.detectChanges();

    expect(postsService.getPosts).toHaveBeenCalledWith(1, 0);
    expect(postsService.posts.length).toEqual(2);
    expect(component.page).toEqual(0);
    expect(component.totalPages).toEqual(1);
    expect(component.isPostsLoading).toBeFalse();
  }));

  it('should get posts', fakeAsync(() => {
    component.getPosts();
    tick();

    fixture.detectChanges();

    expect(postsService.getPosts).toHaveBeenCalledWith(1, 0);
    expect(postsService.posts.length).toEqual(2);
    expect(component.page).toEqual(0);
    expect(component.totalPages).toEqual(1);
    expect(component.isPostsLoading).toBeFalse();
  }));

  it('should not get posts if there\'s no cultural offering', fakeAsync(() => {
    (postsService.getPosts as Spy).calls.reset();
    detailsService.culturalOffering.next(undefined);

    component.getPosts();
    tick();
    fixture.detectChanges();

    expect(postsService.getPosts).not.toHaveBeenCalled();
  }));

  it('should not get posts if page is the last one cultural offering', fakeAsync(() => {
    (postsService.getPosts as Spy).calls.reset();
    component.page = 1;
    component.totalPages = 1;

    component.getPosts();
    tick();
    fixture.detectChanges();

    expect(postsService.getPosts).not.toHaveBeenCalled();
    expect(component.isPostsLoading).toBeFalse();
  }));

  it('should calculate time string based on time', () => {
    expect(component.relativeTimeAdded(moment())).toEqual('few seconds ago');

    expect(component.relativeTimeAdded()).toEqual('some time ago.');
  });

  it('should calculate whether to stick briefinfo card', fakeAsync(() => {
    component.onScroll();

    expect(component.stickBriefInfo.getValue()).toBeFalse();

  }));

  it('should announce a new post', fakeAsync(() => {
    component.newPostContent = 'Novi kontent      ';

    component.onClickAnnounce();

    tick();
    fixture.detectChanges();

    expect(postsService.createPost).toHaveBeenCalledWith('Novi kontent', 1);
    expect(addSpy).toHaveBeenCalled();
    expect(postsService.getPosts).toHaveBeenCalledWith(1, 0);
  }));

  it('should not announce a new post. no content', fakeAsync(() => {
    (addSpy as Spy).calls.reset();
    (postsService.createPost as Spy).calls.reset();

    component.newPostContent = '      ';

    component.onClickAnnounce();


    expect(postsService.createPost).not.toHaveBeenCalled();
    expect(addSpy).toHaveBeenCalled();

    tick();
    fixture.detectChanges();
  }));

  it('should not announce a new post. no content', fakeAsync(() => {
    (addSpy as Spy).calls.reset();
    (postsService.createPost as Spy).calls.reset();

    detailsService.culturalOffering.next(undefined);

    component.onClickAnnounce();

    tick();
    expect(addSpy).toHaveBeenCalled();
    expect(postsService.createPost).not.toHaveBeenCalled();
  }));

  it('should reset posts', fakeAsync(() => {
    component.resetPosts();

    tick();

    fixture.detectChanges();

    expect(postsService.getPosts).toHaveBeenCalledWith(1, 0);
    expect(postsService.posts.length).toEqual(2);
    expect(component.page).toEqual(0);
    expect(component.totalPages).toEqual(1);
    expect(component.isPostsLoading).toBeFalse();
  }));

  it('should get menu items for a post', () => {
    const retObj = component.getMenuItems(getPosts()[0], 0);
    expect(retObj[0].label).toEqual('Edit');
    expect(retObj[0].icon).toEqual('pi pi-pencil');
    expect(retObj[1].label).toEqual('Delete');
    expect(retObj[1].icon).toEqual('pi pi-trash');
  });

  it('should open edit post dialog', fakeAsync(() => {
    component.editPost(getPosts()[0]);

    tick();
    fixture.detectChanges();

    expect(component.isEditDialogOpen).toBeTrue();
    expect(component.editContent).toEqual('Neki kontent');
    expect(component.editingPost).toEqual(getPosts()[0]);
  }));

  it('should edit a post after it has been confirmed', fakeAsync(() => {
    component.editingPost = getPosts()[0];
    component.editContent = 'Editing content  ';
    component.onClickEdit();

    tick();
    fixture.detectChanges();

    expect(postsService.updatePost).toHaveBeenCalledWith(jasmine.any(Post));
    expect(addSpy).toHaveBeenCalled();
    expect(component.editContent).toEqual('');
    expect(component.isEditDialogOpen).toBeFalse();
  }));

  it('should open confirmation dialog', fakeAsync(() => {
    const confirmSpy = spyOn(confirmationService, 'confirm').and.callFake(confirmation => {
      return confirmation.accept();
    });

    component.deletePost(getPosts()[0]);

    tick();
    fixture.detectChanges();

    expect(confirmSpy).toHaveBeenCalled();
  }));

  it('should delete a post', fakeAsync(() => {
    const resetPostsSpy = spyOn(component, 'resetPosts');
    component.postDeletionConfirmed(getPosts()[0]);

    tick();
    fixture.detectChanges();

    expect(postsService.deletePost).toHaveBeenCalledWith(1);
    expect(addSpy).toHaveBeenCalled();
    expect(resetPostsSpy).toHaveBeenCalled();
  }));

  it('should get the user role from authservice', () => {
    expect(component.getUserRole()).toEqual(authService.getUserRole());
  });

  it('should get cultural offering from detailsService', () => {
    const offering = component.culturalOffering;

    expect(offering).toEqual(detailsService.culturalOffering.getValue());
  });

  it('should get posts from postsService', () => {
    expect(component.posts).toEqual(postsService.posts);
  });

  it('should destroy array on destroy', () => {
    component.ngOnDestroy();

    expect(postsService.posts.length).toEqual(0);
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

function getPosts(): Post[] {
  return [
    {
      id: 1,
      culturalOfferingId: 1,
      content: 'Neki kontent',
      timeAdded: moment()
    },
    {
      id: 2,
      culturalOfferingId: 1,
      content: 'Neki drugi kontent',
      timeAdded: moment()
    }
  ];
}
