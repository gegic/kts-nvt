import {Authority} from './authority';

export class User {
  id = '';
  email = '';
  password = '';
  firstName = '';
  lastName = '';
  lastPasswordChange: Date = new Date();
  authorities: Authority[] = [];
  verified = false;

  getRole(): string {
    return this.authorities.find(a => a.authority.startsWith('ROLE'))?.authority.slice(5) ?? '';
  }

  authorize(accessRoles: string[]): boolean {
    return accessRoles.includes(this.getRole());
  }
}
