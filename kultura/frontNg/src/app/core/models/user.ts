export class User {
  id = '';
  email = '';
  firstName = '';
  lastName = '';
  lastPasswordChange: Date = new Date();
  authorities: [] = [];
  logged = false;

  constructor() {}
}
