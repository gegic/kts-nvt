import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {RegisterService} from '../../../../core/services/register/register.service';
import {ActivatedRoute, Router} from '@angular/router';
import {MessageService} from 'primeng/api';

@Component({
  selector: 'app-register-name',
  templateUrl: './register-name.component.html',
  styleUrls: ['./register-name.component.scss']
})
export class RegisterNameComponent implements OnInit {

  nameGroup: FormGroup;

  constructor(private registerService: RegisterService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private messageService: MessageService) {
    this.nameGroup = new FormGroup({
      firstName: new FormControl('', [Validators.required, Validators.pattern(/\p{Lu}\p{L}+/u)]), // unikod veliko slovo
      lastName: new FormControl('', [Validators.required, Validators.pattern(/\p{Lu}\p{L}+/u)]), // praceno malim slovima
    });
  }

  ngOnInit(): void {
    if (!this.registerService.email) {
      this.router.navigate(['..'], {relativeTo: this.activatedRoute});
    }
  }

  onClickProceed(): void {
    if (this.nameGroup.invalid) {
      this.messageService.add({id: 'toast-container', severity: 'error', detail: 'Please enter only the first and the last name with capital first letters (Fields can\'t be empty).'});
      return;
    }
    this.registerService.firstName = this.nameGroup.get('firstName')?.value;
    this.registerService.lastName = this.nameGroup.get('lastName')?.value;
    this.router.navigate(['../password'], {relativeTo: this.activatedRoute});
  }

  onClickBack(): void {
    // this.registerService.
    this.router.navigate(['..'], {relativeTo: this.activatedRoute});
  }

}
