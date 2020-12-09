import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  httpClient:HttpClient;

  constructor(httpClient:HttpClient) {
    this.httpClient = httpClient;

   }

  doLogin(data:Object){
    this.httpClient.post("/auth/login", data);
  }
}
