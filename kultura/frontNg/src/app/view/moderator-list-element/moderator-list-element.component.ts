import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {Moderator} from '../../core/models/moderator';
import {ConfirmationService, MessageService} from 'primeng/api';
import {ModeratorService} from '../../core/services/moderator/moderator.service';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-moderator-list-element',
  templateUrl: './moderator-list-element.component.html',
  styleUrls: ['./moderator-list-element.component.scss']
})
export class ModeratorListElementComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = [];
  @Input()
  moderator !: Moderator;
  @Output()
  moderatorDeleted: EventEmitter<any> = new EventEmitter<any>();
  constructor(private router: Router,
              private confirmationService: ConfirmationService,
              private messageService: MessageService,
              private moderatorService: ModeratorService) { }

  ngOnInit(): void {
  }

  onClickDelete(): void {
    this.confirmationService.confirm(
      {
        message: `Are you sure that you want to delete ${this.moderator?.firstName + ' ' + this.moderator?.lastName ?? ''}`,
        acceptLabel: 'Delete',
        rejectLabel: 'Close',
        header: 'Deletion',
        icon: 'pi pi-trash',
        accept: () => this.deletionConfirmed()
      });
  }

  deletionConfirmed(): void {
    this.subscriptions.push(
      this.moderatorService.delete(this.moderator?.id ?? 0).subscribe(() => {
        this.messageService.add({
          id: 'toast-container',
          severity: 'success',
          summary: 'Deleted successfully',
          detail: 'The moderator was deleted successfully'
        });
        this.moderatorDeleted.emit(this.moderator);
      })
    );
  }

  onClickEdit(): void{
    this.router.navigate([`/edit-moderator/${this.moderator?.id ?? 0}`]).then(r => console.log('EDIT'));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

}
