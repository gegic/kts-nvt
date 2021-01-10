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
    const isAuthRoute: boolean = route.url.filter(
      us => us.path.includes('login') || us.path.includes('register') || us.path.includes('verify')).length === 0;
    const user = Object.assign(new User(), this.authService.user.getValue());
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

      if (route.data.hasOwnProperty('roles')) {
        accessRoles = route.data.roles as string[];
      } else {
        return true;
      }
      if (accessRoles.length === 0) {
        return true;
      }
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
