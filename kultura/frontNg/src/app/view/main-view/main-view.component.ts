import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../core/services/auth/auth.service';
import {HttpClient} from '@angular/common/http';
import {Route, Router} from '@angular/router';
import {User} from '../../core/models/user';

@Component({
  selector: 'app-main-view',
  templateUrl: './main-view.component.html',
  styleUrls: ['./main-view.component.scss']
})
export class MainViewComponent implements OnInit {

  url = '';
  res = '';

  constructor(private authService: AuthService,
              private httpClient: HttpClient,
              private router: Router) { }

  ngOnInit(): void {
  }
  onSendRequest(): void {
    this.httpClient.get(this.url).subscribe(
      data => {
        this.res = JSON.stringify(data);
      }
    );
  }

  get userRole(): string {
    const user: User = Object.assign(new User(), this.authService.user.getValue());
    if (!user) {
      return '';
    }
    const role = (user as User).getRole();
    if (!role) {
      return '';
    }
    return role;
  }
}
