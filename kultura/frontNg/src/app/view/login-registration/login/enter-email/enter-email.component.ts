import {Component, OnDestroy, OnInit} from '@angular/core';
import {LoginService} from '../../../../core/services/login/login.service';
import {FormControl} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {MessageService} from 'primeng/api';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-enter-email',
  templateUrl: './enter-email.component.html',
  styleUrls: ['./enter-email.component.scss']
})
export class EnterEmailComponent implements OnDestroy{
  private subscription?: Subscription;
  emailControl: FormControl;
  loading = false;

  constructor(private loginService: LoginService,
              private router: Router,
              private messageService: MessageService,
              private activatedRoute: ActivatedRoute) {
    this.emailControl = new FormControl();
  }

  onClickProceed(): void {
    this.loading = true;
    this.subscription = this.loginService.checkExistence(this.emailControl.value)
      .subscribe(
        (data: {value: string}) => {
          this.loading = false;
          this.loginService.email = this.emailControl.value;
          this.loginService.name = data.value;
          this.router.navigate(['./password'], {relativeTo: this.activatedRoute});
        },
        () => {
          this.loading = false;
          this.emailControl.reset();
          this.messageService.add({id: 'toast-container', severity: 'error', summary: 'Email not found', detail: 'A user with this email doesn\'t exist.'});
        }
      );
  }

  onClickNewAccount(): void {
    this.loginService.reset();
    this.router.navigate(['/register']);
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }


}
