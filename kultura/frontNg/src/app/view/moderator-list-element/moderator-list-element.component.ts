import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Moderator} from '../../core/models/moderator';
import {ConfirmationService, MessageService} from 'primeng/api';
import {ModeratorService} from '../../core/services/moderator/moderator.service';

@Component({
  selector: 'app-moderator-list-element',
  templateUrl: './moderator-list-element.component.html',
  styleUrls: ['./moderator-list-element.component.css']
})
export class ModeratorListElementComponent implements OnInit {
  @Input()
  moderator !: Moderator;
  @Output()
  moderatorDeleted: EventEmitter<any> = new EventEmitter<any>();
  constructor(private confirmationService: ConfirmationService,
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
    this.moderatorService.delete(this.moderator?.id ?? 0).subscribe(() => {
      this.messageService.add({
        severity: 'success',
        summary: 'Deleted successfully',
        detail: 'The moderator was deleted successfully'
      });
      this.moderatorDeleted.emit(this.moderator.id);
    });
  }

}
