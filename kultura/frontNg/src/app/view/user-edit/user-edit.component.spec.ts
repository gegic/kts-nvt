import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {UserEditComponent} from './user-edit.component';
import {UserService} from '../../core/services/users/users.service';
import {MessageService} from 'primeng/api';
import {AuthService} from '../../core/services/auth/auth.service';
import createSpy = jasmine.createSpy;
import {User} from '../../core/models/user';
import {By} from '@angular/platform-browser';
import {BehaviorSubject, of} from 'rxjs';
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
  let message = '';

  beforeEach(async () => {
    const userServiceMock = {
      update: createSpy('update').and.returnValue(of(getUpdatedUser()))
    };

    const authServiceMock = {
      user: new BehaviorSubject<User | null>(getOldUser()),
      updateUserData: () => {
      },
      logout: createSpy('logout').and.stub()
    };

    const messageServiceMock = {
      add: createSpy('add').and.callFake((msg: any): void => {
        message = msg.severity;
      })
    };


    await TestBed.configureTestingModule({
      declarations: [UserEditComponent],
      imports: [TableModule, TabViewModule, FormsModule, ReactiveFormsModule],
      providers: [
        {provide: UserService, useValue: userServiceMock},
        {provide: AuthService, useValue: authServiceMock},
        {provide: MessageService, useValue: messageServiceMock}
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
    component.ngOnInit();
  });

  it('should create', () => {
    expect(component).toBeDefined();
  });

  it('should have updated name', () => {
    component.name.setValue('Marques');
    component.updateName();
    fixture.detectChanges();
    expect(userService.update).toHaveBeenCalled();
    expect(messageService.add).toHaveBeenCalled();
    expect(message).toEqual('success');
    expect(component.name.value).toEqual(null);
    expect(component.user.firstName).toEqual('Marques');
    expect(fixture.debugElement.query(By.css('#name-val')).nativeElement.textContent).toContain('Marques');
  });

  it('should have updated last name', () => {
    component.lastName.setValue('Queensberry');
    component.updateLastName();
    fixture.detectChanges();
    expect(userService.update).toHaveBeenCalled();
    expect(messageService.add).toHaveBeenCalled();
    expect(message).toEqual('success');
    expect(component.lastName.value).toEqual(null);
    expect(component.user.lastName).toEqual('Queensberry');
    expect(fixture.debugElement.query(By.css('#lastName-val')).nativeElement.textContent).toContain('Queensberry');
  });

  it('should have updated e-mail', () => {
    component.email.setValue('marques@mail.com');
    component.updateEmail();
    fixture.detectChanges();
    expect(userService.update).toHaveBeenCalled();
    expect(messageService.add).toHaveBeenCalled();
    expect(message).toEqual('info');
    expect(authService.logout).toHaveBeenCalled();
    expect(component.email.value).toEqual(null);
    expect(component.user.email).toEqual('marques@mail.com');
    expect(fixture.debugElement.query(By.css('#email-val')).nativeElement.textContent).toContain('marques@mail.com');
  });

  it('should have invalid name', () => {
    component.name.setValue('');
    component.updateName();

    expect(messageService.add).toHaveBeenCalled();
    expect(message).toEqual('error');
    expect(component.name.invalid).toBeTruthy();
  });
  it('should have invalid last name', () => {
    component.lastName.setValue('');
    component.updateLastName();
    expect(messageService.add).toHaveBeenCalled();
    expect(message).toEqual('error');
    expect(component.lastName.invalid).toBeTruthy();
  });
  it('should have invalid e-mail', () => {
    component.email.setValue('');
    component.updateEmail();
    expect(messageService.add).toHaveBeenCalled();
    expect(message).toEqual('error');
    expect(component.email.invalid).toBeTruthy();
  });
  it('should have invalid e-mail format', () => {
    component.email.setValue('a@.com');
    component.updateEmail();
    expect(messageService.add).toHaveBeenCalled();
    expect(message).toEqual('error');
    expect(component.email.invalid).toBeTruthy();
  });


  it('should have empty password', () => {
    component.passwordControl.setValue('');
    component.updatePassword();
    expect(messageService.add).toHaveBeenCalled();
    expect(message).toEqual('error');
    expect(component.email.invalid).toBeTruthy();
  });
  it('should have short password', () => {
    component.passwordControl.setValue('aaa');
    component.updatePassword();
    expect(messageService.add).toHaveBeenCalled();
    expect(message).toEqual('error');
    expect(component.email.invalid).toBeTruthy();
  });
  it('should have too long password', () => {
    component.passwordControl.setValue('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa');
    component.updatePassword();
    expect(messageService.add).toHaveBeenCalled();
    expect(message).toEqual('error');
    expect(component.email.invalid).toBeTruthy();
  });
  it('should have password without capital letters', () => {
    component.passwordControl.setValue('aaaaaaaa');
    component.updatePassword();
    expect(messageService.add).toHaveBeenCalled();
    expect(message).toEqual('error');
    expect(component.email.invalid).toBeTruthy();
  });
  it('should have password without digits', () => {
    component.passwordControl.setValue('Aaaaaaaa');
    component.updatePassword();
    expect(messageService.add).toHaveBeenCalled();
    expect(message).toEqual('error');
    expect(component.email.invalid).toBeTruthy();
  });
  it('should have empty confirm password field', () => {
    component.passwordControl.setValue('Aaaaaaa1');
    component.updatePassword();
    expect(messageService.add).toHaveBeenCalled();
    expect(message).toEqual('error');
  });


  it('should have different passwords', () => {
    component.passwordControl.setValue('Aaaaaaa1');
    component.confirmPasswordControl.setValue('Aaaaaaaa');
    component.updatePassword();
    expect(messageService.add).toHaveBeenCalled();
    expect(message).toEqual('error');
  });

  it('should update password', () => {
    component.passwordControl.setValue('Aaaaaaa1');
    component.confirmPasswordControl.setValue('Aaaaaaa1');
    component.updatePassword();
    expect(messageService.add).toHaveBeenCalled();
    expect(message).toEqual('info');
    expect(authService.logout).toHaveBeenCalled();

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
