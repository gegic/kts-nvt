import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ModeratorService} from '../../core/services/moderator/moderator.service';
import {MessageService} from 'primeng/api';
import {Moderator} from '../../core/models/moderator';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-moderator-add',
  templateUrl: './moderator-add.component.html',
  styleUrls: ['./moderator-add.component.scss']
})
export class ModeratorAddComponent implements OnInit, OnDestroy {

  private subscriptions: Subscription[] = [];

  moderatorForm: FormGroup = new FormGroup(
    {
      firstName: new FormControl(undefined, [Validators.required, Validators.pattern(/[\p{L} \d]+/u)]),
      lastName: new FormControl(undefined, [Validators.required, Validators.pattern(/[\p{L} \d]+/u)]),
      password: new FormControl(undefined, [Validators.required, Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/)]),
      repeatPassword: new FormControl(undefined, [Validators.required,
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/)]),
      email: new FormControl(undefined, [Validators.required, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,8}$')]),
    }
  );

  constructor(private moderatorService: ModeratorService,
              private messageService: MessageService,
              private router: Router) {
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    if (this.moderatorForm.controls.firstName.invalid) {
      this.messageService.add({id: 'toast-container', severity: 'error', detail: 'First name cannot be empty and must start with a capital letter'});
      this.moderatorForm.patchValue({firstName: ''});
    }
    if (this.moderatorForm.controls.lastName.invalid) {
      this.messageService.add({id: 'toast-container', severity: 'error', detail: 'Last name cannot be empty and must start with a capital letter'});
      this.moderatorForm.patchValue({lastName: ''});
    }
    if (this.moderatorForm.controls.email.invalid) {
      this.messageService.add({id: 'toast-container', severity: 'error', detail: 'Enter the mail in the correct format'});
      this.moderatorForm.patchValue({email: ''});
    }
    if (this.moderatorForm.controls.password.invalid) {
      this.messageService.add({
        id: 'toast-container',
        severity: 'error', detail: 'Password has to contain at least one uppercase, ' +
          'one lowercase letter and one digit. It has to be at least 8 characters long'
      });
      this.moderatorForm.patchValue({password: '', repeatPassword: ''});
    }
    if (this.moderatorForm.controls.password.value !== this.moderatorForm.controls.repeatPassword.value) {
      this.messageService.add({
        id: 'toast-container',
        severity: 'error',
        detail: 'Repeated password has to match the original password.'
      });
      this.moderatorForm.patchValue({password: '', repeatPassword: ''});
    }
    const moderator: Moderator = {
      email : this.moderatorForm.controls.email.value,
      password : this.moderatorForm.controls.password.value,
      firstName : this.moderatorForm.controls.firstName.value,
      lastName : this.moderatorForm.controls.lastName.value,
    };
    if (this.moderatorForm.invalid) {
      return;
    }
    this.subscriptions.push(
      this.moderatorService.createModerator(moderator).subscribe(
        () => {
          this.messageService.add({
            id: 'toast-container',
            severity: 'success',
            summary: 'Moderator added successfully.',
            detail: 'All that remains is for the moderator to verify the profile.'
          });
          this.router.navigate(['']);
        },
        () => {
          this.messageService.add({
            id: 'toast-container',
            severity: 'error',
            summary: 'Moderator couldn\'t be added',
            detail: 'This moderator couln\'t be added due to the fact the email already exists in the database'
          });
        }
      )
    );
  }

  ngOnDestroy(): void {
  }

}
