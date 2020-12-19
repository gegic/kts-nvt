import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../core/services/auth/auth.service';
import {MenuItem} from 'primeng/api';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  menuItems: MenuItem[] = [
    {label: 'Logout', icon: 'pi pi-fw pi-power-off', command: e => this.onClickLogout()}
  ];

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
  }

  onClickLogout(): void {
    this.authService.logout();
  }

  get name(): string {
    return `${this.authService.user.getValue()?.firstName} ${this.authService.user.getValue()?.lastName}`;
  }

}
