import { Component, OnInit } from '@angular/core';
import {ModeratorService} from '../../core/services/moderator/moderator.service';

@Component({
  selector: 'app-moderators-view',
  templateUrl: './moderators-view.component.html',
  styleUrls: ['./moderators-view.component.css']
})
export class ModeratorsViewComponent implements OnInit {

  moderatorsList: [] | undefined = [];

  constructor(private moderatorService: ModeratorService) { }

  ngOnInit(): void {
    this.moderatorService.getModerators().subscribe(moderators => {
      this.moderatorsList = moderators.content;
    });
  }

  onClickLogout(): void {
    this.moderatorService.info();
    console.log(this.moderatorService.getModerators());
  }

}
