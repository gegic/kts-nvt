import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../core/services/auth/auth.service';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-admin-view',
  templateUrl: './admin-view.component.html',
  styleUrls: ['./admin-view.component.scss']
})
export class AdminViewComponent implements OnInit {

  url = '';
  res = '';

  constructor(private authService: AuthService,
              private httpClient: HttpClient) { }

  ngOnInit(): void {
  }

  onLogout(): void {
    this.authService.logout();
  }

  onSendRequest(): void {
    this.httpClient.get(this.url).subscribe(
      data => {
        this.res = JSON.stringify(data);
      }
    );
  }

  sendRequest()

}
