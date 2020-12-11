import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Route, Router} from '@angular/router';
import {VerifyService} from '../../../../core/services/verify/verify.service';
import {MessageService} from 'primeng/api';

@Component({
  selector: 'app-register-verify',
  templateUrl: './register-verify.component.html',
  styleUrls: ['./register-verify.component.scss']
})
export class RegisterVerifyComponent implements OnInit {

  id = '';
  verified = false;

  constructor(private activatedRoute: ActivatedRoute,
              private verifyService: VerifyService,
              private router: Router,
              private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap
      .subscribe(
        p => {
          const id = p.get('id');
          if (id === null) {
            this.router.navigate(['/login']);
          }
          this.id = id as string;
        }
      );

    this.verifyService.checkExistence(this.id).subscribe(
      (data: {value: string}) => {
        this.verifyService.name = data.value;
      }
    );
  }

  onClickVerify(): void {
    this.verifyService.verify(this.id).subscribe(
      () => {
        this.verified = true;
      },
      () => {
        this.messageService.add({severity: 'error', detail: 'An unexpected error occurred.'});
      }
    );
  }

  get name(): string {
    return this.verifyService.name;
  }

}
