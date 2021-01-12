import {Component, OnInit} from '@angular/core';
import {ModeratorService} from '../../core/services/moderator/moderator.service';

@Component({
  selector: 'app-moderators-view',
  templateUrl: './moderators-view.component.html',
  styleUrls: ['./moderators-view.component.css']
})
export class ModeratorsViewComponent implements OnInit {

  moderatorsList: any[] = [];

  constructor(private moderatorService: ModeratorService) {
  }

  ngOnInit(): void {
    this.moderatorService.getModerators().subscribe(moderators => {
      this.moderatorsList = moderators.content || [];
    });
  }

  deleteUser(id: string): void{
    let result: any[] = [];
    result = this.moderatorsList.filter(x => x.id !== id);
    this.moderatorsList = result || [];
  }

}
