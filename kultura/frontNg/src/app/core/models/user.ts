import {Authority} from './authority';
import {Moment} from 'moment-timezone';
import * as moment from 'moment-timezone';

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
