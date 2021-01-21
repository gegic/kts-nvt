import {Authority} from './authority';

export class Moderator {
  id ?: string;
  email ?: string;
  password ?: string;
  firstName ?: string;
  lastName ?: string;
  lastPasswordChange?: string | undefined;
  authorities?: Authority[];
  verified ?: boolean;
}
