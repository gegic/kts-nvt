import {Authority} from './authority';
import * as moment from 'moment-timezone';
import {Moment} from 'moment-timezone';

export class User {
  id = -1;
  email = '';
  password = '';
  firstName = '';
  lastName = '';
  lastPasswordChange: Moment = moment().tz('UTC');
  authorities: Authority[] = [];
  verified = false;

  getRole(): string {
    return this.authorities.find(a => a.authority.startsWith('ROLE'))?.authority.slice(5) ?? '';
  }
}
