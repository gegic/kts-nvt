import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ModeratorService} from '../../core/services/moderator/moderator.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Moderator} from '../../core/models/moderator';
import {MessageService} from 'primeng/api';
import {RegisterService} from '../../core/services/register/register.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-moderator-edit',
  templateUrl: './moderator-edit.component.html',
  styleUrls: ['./moderator-edit.component.scss']
})
export class ModeratorEditComponent implements OnInit, OnDestroy {

  private subscriptions: Subscription[] = [];

  oldModerator: Moderator | undefined;
  moderatorForm: FormGroup = new FormGroup(
    {
      id : new FormControl(undefined),
      firstName: new FormControl(undefined, [Validators.required, Validators.pattern(/[\p{L} \d]+/u)]),
      lastName: new FormControl(undefined, [Validators.required, Validators.pattern(/[\p{L} \d]+/u)]),
      password: new FormControl(undefined, [Validators.required, Validators.pattern('^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}')]),
      repeatPassword: new FormControl(undefined, [Validators.required, Validators.pattern('^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}')]),
      email: new FormControl(undefined, [Validators.required, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,8}$')]),
    }
  );
  constructor(private activatedRoute: ActivatedRoute,
              private moderatorService: ModeratorService,
              private messageService: MessageService,
              private registerService: RegisterService,
              private router: Router) { } // ma dobro no eto

  ngOnInit(): void {
    this.subscriptions.push(
      this.activatedRoute.params.subscribe(
        val => {
          this.moderatorService.getModeratorById(val.id).subscribe(
            data => {
              this.moderatorForm.patchValue({
                id : val.id,
                firstName: data.firstName,
                lastName : data.lastName,
                email : data.email
              });
              this.oldModerator = data;
            }
          );
        }
      )
    );
  }

  onSubmit(): void {
    let hasErrors = false;
    const moderator: Moderator = {
      id : this.moderatorForm.controls.id.value,
      email : this.moderatorForm.controls.email.value,
      verified: this.oldModerator?.verified
    };
    if (this.moderatorForm.controls.firstName.value.trim() === '' ) {
    } else {
      if (this.moderatorForm.controls.firstName.invalid){
        hasErrors = true;
        this.messageService.add({severity: 'error', detail: 'First name cannot be empty and must start with a capital letter'});
      }
      else{
      moderator.firstName = this.moderatorForm.controls.firstName.value;
      }
    }

    if (this.moderatorForm.controls.lastName.value.trim() === '' ) {
    } else {
      if (this.moderatorForm.controls.lastName.invalid){
        hasErrors = true;
        this.messageService.add({severity: 'error', detail: 'Last name name cannot be empty and must start with a capital letter'});
      }
      else{
        moderator.lastName = this.moderatorForm.controls.lastName.value;
      }
    }

    if (this.moderatorForm.controls.email.value.trim() !== '' ) {
      if (this.moderatorForm.controls.email.invalid){
        hasErrors = true;

        this.messageService.add({severity: 'error', detail: 'Enter the mail in the correct format'}); }
      else{
        this.subscriptions.push(
          this.registerService.getIdByMail(this.moderatorForm.controls.email.value)
            .subscribe(
              data => {
                if (data.value === this.moderatorForm.controls.id.value) {
                  this.moderatorForm.patchValue({
                    email: this.moderatorForm.controls.email.value});
                }
                else{
                  hasErrors = true;
                  this.messageService.add({
                    severity: 'error', summary: 'Email already exists',
                    detail: 'An account with this email already exists.'
                  });
                  this.moderatorForm.patchValue({
                    email: this.oldModerator?.email
                  });
                }
              },
              () => {
                this.moderatorForm.patchValue({
                  email: this.moderatorForm.controls.email.value});
              }
            )
        );
      }
    }

    if (this.moderatorForm.controls.password.value ||  this.moderatorForm.controls.repeatPassword.value) {
      if (this.moderatorForm.controls.password.invalid) {
        hasErrors = true;
        this.messageService.add({
          severity: 'error', detail: 'Password has to contain at least one uppercase, ' +
            'one lowercase letter and one digit. It has to be at least 8 characters long'
        });
      }
      else if (this.moderatorForm.controls.password.value !== this.moderatorForm.controls.repeatPassword.value) {
        hasErrors = true;
        this.messageService.add({
          severity: 'error',
          detail: 'Repeated password has to match the original password.'
        });
      }
      else {
        // moderator.lastPasswordChange = moderator.password;
        moderator.password = this.moderatorForm.controls.password.value;
      }
    }

    if (hasErrors){
      return;
    }
    this.subscriptions.push(
      this.moderatorService.updateModerator(moderator).subscribe(
        () => {
          this.messageService.add({
            severity: 'success', summary: 'Moderator updated successfully.'
          });
          this.router.navigate(['/moderators']);
        }
      )
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }
}
