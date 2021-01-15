import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
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
    const user = Object.assign(new User(), this.authService.user.getValue());
    const token: string | null = this.authService.token.getValue();
    const isTokenExpired: boolean = !!token ? this.jwtHelper.isTokenExpired(token) : true;
    const isAuthenticated = !isTokenExpired && user.id !== -1;

    let accessRoles: string[] = [];
    if (route.data.hasOwnProperty('roles')) {
      accessRoles = route.data.roles as string[];
    } else {
      return true;
    }
    if (accessRoles.length === 0) {
      return false;
    }
    if (!isAuthenticated && accessRoles.includes('UNREGISTERED')) {
      return true;
    } else if (!isAuthenticated && !accessRoles.includes('UNREGISTERED')) {
      return false;
    } else {
      const auth = this.authorize(accessRoles, user) ?? false;
      if (!auth) {
        if (user.getRole() === 'ADMIN') {
          this.router.navigate(['admin-panel']);
        } else {
          this.router.navigate(['']);
        }
      }
    }

    return true;
  }

  private authorize(accessRoles: string[], user: User | null): boolean {
    if (!user) {
      return false;
    }
    return accessRoles.includes(user.getRole());
  }
}
