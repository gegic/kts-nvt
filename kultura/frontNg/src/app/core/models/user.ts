export class User {
  id = '';
  email = '';
  password = '';
  firstName = '';
  lastName = '';
  lastPasswordChange: Date = new Date();
  authorities: [] = [];
  logged = false;

  constructor() {}
}
