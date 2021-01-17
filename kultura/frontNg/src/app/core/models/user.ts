import {Authority} from './authority';
import {Moment} from 'moment-timezone';
import * as moment from 'moment-timezone';

export class User {
  id = 0;
  email: string | null = '';
  password: string | null = '';
  firstName: string | null = '';
  lastName: string | null = '';
  lastPasswordChange: Moment | null = moment().tz('UTC');
  authorities: Authority[] | null = [];
  verified: boolean | null = false;

  getRole(): string {
    return this.authorities?.find(a => a.authority.startsWith('ROLE'))?.authority.slice(5) ?? '';
  }
}
