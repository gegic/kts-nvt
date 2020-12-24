import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ModeratorService} from '../../core/services/moderator/moderator.service';
import {User} from '../../core/models/user';
import {MessageService} from 'primeng/api';
import {RegisterService} from '../../core/services/register/register.service';
import {Moderator} from '../../core/models/moderator';
import {Router} from '@angular/router';

@Component({
  selector: 'app-moderator-add',
  templateUrl: './moderator-add.component.html',
  styleUrls: ['./moderator-add.component.css']
})
export class ModeratorAddComponent implements OnInit {
  moderatorForm: FormGroup = new FormGroup(
    {
      firstName: new FormControl(undefined, [Validators.required, Validators.pattern(/[\p{L} \d]+/u)]),
      lastName: new FormControl(undefined, [Validators.required, Validators.pattern(/[\p{L} \d]+/u)]),
      password: new FormControl(undefined, [Validators.required, Validators.pattern('^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}')]),
      repeatPassword: new FormControl(undefined, [Validators.required, Validators.pattern('^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}')]),
      email: new FormControl(undefined, [Validators.required, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')]),
    }
  );

  constructor(private registerService: RegisterService,
              private moderatorService: ModeratorService,
              private messageService: MessageService,
              private router: Router) {
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    console.log('ADD MODERATOR');
    console.log(this.moderatorForm.controls);
    if (this.moderatorForm.controls.firstName.invalid) {
      this.messageService.add({severity: 'error', detail: 'First name cannot be empty and must start with a capital letter'});
      this.moderatorForm.patchValue({firstName: ''});
    }
    if (this.moderatorForm.controls.lastName.invalid) {
      this.messageService.add({severity: 'error', detail: 'Last name cannot be empty and must start with a capital letter'});
      this.moderatorForm.patchValue({lastName: ''});
    }
    if (this.moderatorForm.controls.email.invalid) {
      this.messageService.add({severity: 'error', detail: 'Enter the mail in the correct format'});
      this.moderatorForm.patchValue({email: ''});
    }
    if (this.moderatorForm.controls.password.invalid) {
      this.messageService.add({
        severity: 'error', detail: 'Password has to contain at least one uppercase, ' +
          'one lowercase letter and one digit. It has to be at least 8 characters long'
      });
      this.moderatorForm.patchValue({password: '', repeatPassword: ''});
    }
    if (this.moderatorForm.controls.password.value !== this.moderatorForm.controls.repeatPassword.value) {
      this.messageService.add({
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
    console.log(this.moderatorForm.controls.firstName.invalid);
    if (this.moderatorForm.invalid) {
      return;
    }
    this.registerService.checkExistence(this.moderatorForm.controls.email.value)
      .subscribe(
        data => {
          this.messageService.add({
            severity: 'error', summary: 'Email already exists',
            detail: 'An account with this email already exists.'
          });
          this.moderatorForm.patchValue({email: ''});
        },
        error => {
          this.moderatorService.createModerator(moderator).subscribe(
            data => {
              this.messageService.add({
                severity: 'success', summary: 'Moderator added successfully.',
                detail: 'All that remains is for the moderator to verify the profile.'
              });
              this.router.navigate(['/']);
            }
          );
        }
      );
  }

}
