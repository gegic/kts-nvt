import {Authority} from './authority';

export class Moderator{
  id ?: string;
  email ?: string;
  password ?: string;
  firstName ?: string;
  lastName ?: string;
  lastPasswordChange?: Date;
  authorities?: Authority[];
  verified ?: boolean;
}
