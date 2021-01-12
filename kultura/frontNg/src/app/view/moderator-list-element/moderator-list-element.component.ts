import {Component, Input, OnInit} from '@angular/core';
import {Moderator} from '../../core/models/moderator';
import {ConfirmationService, MessageService} from 'primeng/api';

@Component({
  selector: 'app-moderator-list-element',
  templateUrl: './moderator-list-element.component.html',
  styleUrls: ['./moderator-list-element.component.css']
})
export class ModeratorListElementComponent implements OnInit {
  @Input()
  moderator !: Moderator;
  constructor(private confirmationService: ConfirmationService,
              private messageService: MessageService) { }

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
    console.log('Djes baaaaaa brise li');
  }

}
