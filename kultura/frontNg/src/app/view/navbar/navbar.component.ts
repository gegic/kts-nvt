import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from '../../core/services/auth/auth.service';
import {MenuItem} from 'primeng/api';
import {CulturalOfferingsService} from '../../core/services/cultural-offerings/cultural-offerings.service';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit, OnDestroy {

  private subscriptions: Subscription[] = [];

  searchQuery = '';

  menuItems: MenuItem[] = [
    {label: 'Edit account', icon: 'pi pi-fw pi-user-edit', routerLink: ['/user-edit']},
    {label: 'Logout', icon: 'pi pi-fw pi-power-off', command: e => this.onClickLogout(), id: 'logout-btn'}
  ];

  constructor(private authService: AuthService,
              private culturalOfferingsService: CulturalOfferingsService,
              private router: Router) { }

  ngOnInit(): void {
    this.subscriptions.push(
      this.culturalOfferingsService.searchQuery.subscribe(val => {
        this.searchQuery = val;
      })
    );
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

  onClickSignIn(): void {
    this.router.navigate(['login']);
  }

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  getUserRole(): string {
    return this.authService.getUserRole();
  }

  isLinkActive(url: string): boolean {
    const queryParamsIndex = this.router.url.indexOf('?');
    let baseUrl = queryParamsIndex === -1 ? this.router.url : this.router.url.slice(0, queryParamsIndex);
    if (baseUrl === url) {
      return true;
    }
    if (baseUrl.startsWith('/')) {
      baseUrl = baseUrl.slice(1);
    }
    if (baseUrl === url) {
      return true;
    }
    if (baseUrl.endsWith('/')) {
      baseUrl = baseUrl.slice(0, -1);
    }
    return baseUrl === url;
  }

  get name(): string {
    return `${this.authService.user.getValue()?.firstName} ${this.authService.user.getValue()?.lastName}`;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

}
