import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router, UrlSegment} from '@angular/router';
import {Observable, throwError} from 'rxjs';
import {AuthService} from '../../services/auth/auth.service';
import {JwtHelperService} from '@auth0/angular-jwt';
import {User} from '../../models/user';

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
    const isAuthRoute: boolean = route.url.filter(
      us => us.path.includes('login') || us.path.includes('register') || us.path.includes('verify')).length === 0;
    const user = this.authService.user.getValue();
    const token: string | null = this.authService.token.getValue();
    const isTokenExpired: boolean = !!token ? this.jwtHelper.isTokenExpired(token) : true;
    const isAuthenticated = !isTokenExpired && !!user;
    if (!isAuthRoute && isAuthenticated) {
      this.router.navigateByUrl('/');
      return false;
    } else if (isAuthRoute && !isAuthenticated) {
      this.router.navigateByUrl('/login');
      return false;
    }

    if (isAuthRoute) { // if everything is alright but the route is authorized, check for the user roles
      let accessRoles: string[] = [];
      if (!route.children || route.children.length < 1) {
        return true;
      }
      if (route.data.hasOwnProperty('roles')){
        accessRoles = route.data.roles as string[];
      } else {
        return true;
      }
      if (accessRoles.length === 0) {
        return true;
      }
      return user?.authorize(accessRoles) ?? false;
    }

    return true;
  }

}
