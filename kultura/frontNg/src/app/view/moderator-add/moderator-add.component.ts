import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-moderator-add',
  templateUrl: './moderator-add.component.html',
  styleUrls: ['./moderator-add.component.css']
})
export class ModeratorAddComponent implements OnInit {
  moderator = {
    firstName: ''
  };
  constructor() { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    console.log('ADD MODERATOR');
    console.log(this.moderator.firstName);
    this.moderator.firstName = '';
  }

}
