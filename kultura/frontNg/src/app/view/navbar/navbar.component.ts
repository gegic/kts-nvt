import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../core/services/auth/auth.service';
import {MenuItem} from 'primeng/api';
import {CulturalOfferingsService} from '../../core/services/cultural-offerings/cultural-offerings.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  searchQuery = '';

  menuItems: MenuItem[] = [
    {label: 'Logout', icon: 'pi pi-fw pi-power-off', command: e => this.onClickLogout()}
  ];

  constructor(private authService: AuthService,
              private culturalOfferingsService: CulturalOfferingsService,
              private router: Router) { }

  ngOnInit(): void {
  }

  onClickLogout(): void {
    this.authService.logout();
  }

  setSearchQuery(event: any): void {
    if (event.key === 'Enter') {
      this.culturalOfferingsService.searchQuery.next(this.searchQuery);
      this.router.navigate(['list-view']);
    }
  }

  get name(): string {
    return `${this.authService.user.getValue()?.firstName} ${this.authService.user.getValue()?.lastName}`;
  }

}
