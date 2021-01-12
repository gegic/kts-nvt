import {Component, Input, OnInit} from '@angular/core';
import {Moderator} from '../../core/models/moderator';

@Component({
  selector: 'app-moderator-list-element',
  templateUrl: './moderator-list-element.component.html',
  styleUrls: ['./moderator-list-element.component.css']
})
export class ModeratorListElementComponent implements OnInit {
  @Input()
  moderator !: Moderator;
  constructor() { }

  ngOnInit(): void {
  }

}
