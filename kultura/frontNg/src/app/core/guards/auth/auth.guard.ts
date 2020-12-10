import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router, UrlSegment} from '@angular/router';
import { Observable } from 'rxjs';
import {AuthService} from '../../services/auth/auth.service';
import {JwtHelperService} from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  private jwtHelper: JwtHelperService = new JwtHelperService();

  constructor(private authService: AuthService,
              private router: Router) {
  }
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const isAuthRoute: boolean = route.url.filter(us => us.path.includes('login') || us.path.includes('register')).length === 0;
    const isAuthenticated: boolean = !!this.authService.user.getValue();
    const token: string | null = this.authService.token.getValue();
    const isTokenExpired: boolean = !!token ? this.jwtHelper.isTokenExpired(token) : true;
    if (!isAuthRoute && isAuthenticated) {
      this.router.navigateByUrl('/');
      return false;
    } else if (isAuthRoute && !isAuthenticated) {
      this.router.navigateByUrl('/login');
      return false;
    } else if (isAuthRoute && isTokenExpired) {
      this.router.navigateByUrl('/login');
      return false;
    }

    return true;
  }

}
