import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ModeratorService} from '../../core/services/moderator/moderator.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Moderator} from '../../core/models/moderator';
import {MessageService} from 'primeng/api';

@Component({
  selector: 'app-moderator-edit',
  templateUrl: './moderator-edit.component.html',
  styleUrls: ['./moderator-edit.component.css']
})
export class ModeratorEditComponent implements OnInit {
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
              private router: Router) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(
      val => {
        this.moderatorService.getModeratorById(val.id).subscribe(
          data => {
            console.log(data);
            this.moderatorForm.patchValue({
              id : val.id,
              firstName: data.firstName,
              lastName : data.lastName,
              email : data.email
            });
            this.moderatorForm.controls.email.disable();
          }
        );
        console.log(val);
      }
    );
  }

  onSubmit(): void {
    console.log(this.moderatorForm);
    const moderator: Moderator = {
      id : this.moderatorForm.controls.id.value,
      email: this.moderatorForm.controls.email.value
    };
    if (this.moderatorForm.controls.firstName.value.trim() === '' ) {
      this.moderatorForm.patchValue({firstName: ''});
    } else {
      if (this.moderatorForm.controls.firstName.invalid){
        this.messageService.add({severity: 'error', detail: 'First name cannot be empty and must start with a capital letter'});
        this.moderatorForm.patchValue({firstName: ''});
      }
      else{
      moderator.firstName = this.moderatorForm.controls.firstName.value;
      }
    }

    if (this.moderatorForm.controls.lastName.value.trim() === '' ) {
      this.moderatorForm.patchValue({lastName: ''});
    } else {
      if (this.moderatorForm.controls.lastName.invalid){
        this.messageService.add({severity: 'error', detail: 'Last name name cannot be empty and must start with a capital letter'});
        this.moderatorForm.patchValue({lastName: ''});
      }
      else{
        moderator.lastName = this.moderatorForm.controls.lastName.value;
      }
    }
    this.moderatorService.updateModerator(moderator).subscribe(
      data => {
        this.messageService.add({
          severity: 'success', summary: 'Moderator updated successfully.',
          detail: 'All that remains is for the moderator to verify the profile.'
        });
        this.router.navigate(['/admin-panel']);
      }
    );

  }
}
