import {Component, OnDestroy, OnInit} from '@angular/core';
import {ModeratorService} from '../../core/services/moderator/moderator.service';
import {Subscription} from 'rxjs';
import {Moderator} from '../../core/models/moderator';
import {MessageService} from 'primeng/api';

@Component({
  selector: 'app-moderators-view',
  templateUrl: './moderators-view.component.html',
  styleUrls: ['./moderators-view.component.scss']
})
export class ModeratorsViewComponent implements OnInit, OnDestroy {

  private subscriptions: Subscription[] = [];

  page = -1;
  totalPages = 0;
  isModeratorsLoading = false;

  constructor(private moderatorService: ModeratorService) {
  }

  ngOnInit(): void {
    this.resetModerators();
  }

  resetModerators(): void {
    this.isModeratorsLoading = true;
    this.moderatorService.moderators = [];
    this.page = -1;
    this.totalPages = 0;
    this.getModerators();
  }

  onScrollDown(): void {
    this.getModerators();
  }

  getModerators(): void {
    if (this.page === this.totalPages) {
      this.isModeratorsLoading = false;
      return;
    }
    this.isModeratorsLoading = true;
    this.subscriptions.push(
      this.moderatorService.getModerators(this.page + 1).subscribe(
        val => {
          for (const el of val.content) {
            if (this.moderatorService.moderators.some(mod => mod.id === el.id)) {
              continue;
            }
            this.moderatorService.moderators.push(el);
          }
          this.page = val.pageable.pageNumber;
          this.totalPages = val.totalPages;
          this.isModeratorsLoading = false;
        }
      )
    );
  }

  moderatorDeletionConfirmed(): void {

    this.resetModerators();

  }

  get moderators(): Moderator[] {
    return this.moderatorService.moderators;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }
}
