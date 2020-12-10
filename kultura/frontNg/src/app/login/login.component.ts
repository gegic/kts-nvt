import { Component, OnInit } from '@angular/core';
import { LoginService} from '../login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  email:string = "";
  pword:string = "";

  passwordStage:boolean = false;

  loginService:LoginService;

  constructor(loginService:LoginService) { 
    this.loginService = loginService;

  }

  ngOnInit(): void {
  }
  
  onProceed(){
    if(this.email){
      this.passwordStage = true;
    }
  }  
  signin(){
    this.loginService.doLogin({
      email:this.email,
      password:this.pword
    });
  }

}
