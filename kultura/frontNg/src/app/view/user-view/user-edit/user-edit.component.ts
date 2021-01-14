import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})



export class UserEditComponent implements OnInit {


  user:any;

  constructor() { 
    this.user = {
      email:"a@a.com",
      firstName:"Dzoni",
      lastName:"Dep",
    }
  }

  ngOnInit(): void {
  }

}
