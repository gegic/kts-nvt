import {Authority} from './authority';
import {Moment} from 'moment-timezone';

export class Moderator{
  id ?: string;
  email ?: string;
  password ?: string;
  firstName ?: string;
  lastName ?: string;
  lastPasswordChange?: string | undefined;
  authorities?: Authority[];
  verified ?: boolean;
}
