import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {UserEditComponent} from './user-edit.component';
import {UserService} from '../../core/services/users/users.service';
import {MessageService} from 'primeng/api';
import {AuthService} from '../../core/services/auth/auth.service';
import createSpy = jasmine.createSpy;
import {User} from '../../core/models/user';
import {By} from '@angular/platform-browser';
import {BehaviorSubject} from 'rxjs';
import {TabViewModule} from 'primeng/tabview';
import {TableModule} from 'primeng/table';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AvatarModule} from 'ngx-avatar';

describe('UserEditComponent', () => {
  let component: UserEditComponent;
  let fixture: ComponentFixture<UserEditComponent>;
  let authService: AuthService;
  let userService: UserService;
  let messageService: MessageService;

  beforeEach(async () => {
    const userServiceMock = {
      update: createSpy('update').and.returnValue(getUpdatedUser)
    };
    const authServiceMock = {
      user: new BehaviorSubject<User | null>(getOldUser())
    };
    await TestBed.configureTestingModule({
      declarations: [UserEditComponent],
      imports: [TableModule, TabViewModule, FormsModule, ReactiveFormsModule],
      providers: [
        {provide: UserService, useValue: userServiceMock},
        {provide: AuthService, useValue: authServiceMock},
        {provide: MessageService, useValue: new MessageService()}
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    authService = TestBed.inject(AuthService);
    userService = TestBed.inject(UserService);
    messageService = TestBed.inject(MessageService);
    fixture = TestBed.createComponent(UserEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    component.ngOnInit()
  });

  it('should create', () => {
    expect(component).toBeDefined();
  });

  it('should display user data in table', fakeAsync(() => {
        tick();
        fixture.detectChanges();
        const user: User = getOldUser();
        const firstName = fixture.debugElement.query(By.css('#name-val'));
        const lastName = fixture.debugElement.query(By.css('#lastName-val'));
        const email = fixture.debugElement.query(By.css('#email-val'));

        expect(firstName).toBeTruthy();
        expect(lastName).toBeTruthy();
        expect(email).toBeTruthy();

        expect(firstName.nativeElement.textContent).toContain(user.firstName);
        expect(lastName.nativeElement.textContent).toContain(user.lastName);
        expect(email.nativeElement.textContent).toContain(user.email);
      }
    )
  );

  it('should have invalid name', () => {
    component.name.setValue('');
    component.updateName();
    expect(component.name.invalid).toBeTruthy();
  });
  it('should have invalid last name', () => {
    component.lastName.setValue('');
    component.updateLastName();
    expect(component.lastName.invalid).toBeTruthy();
  });
  it('should have invalid name', () => {
    component.email.setValue('');
    component.updateEmail();
    expect(component.email.invalid).toBeTruthy();
  });

  it('should have invalid name', () => {
    component.email.setValue('a@a');
    component.updateEmail();
    expect(component.email.invalid).toBeTruthy();
  });
});

function getOldUser(): User {
  const u = new User();
  u.firstName = 'John';
  u.lastName = 'Johnny';
  u.email = 'john@mail.com';
  u.id = 1;
  return u;
}

function getUpdatedUser(): User {
  const u = new User();
  u.firstName = 'Marques';
  u.lastName = 'Queensberry';
  u.email = 'marques@mail.com';
  u.id = 1;
  return u;
}
