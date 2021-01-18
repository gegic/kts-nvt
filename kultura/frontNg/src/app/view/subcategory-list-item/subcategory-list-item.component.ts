import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Category} from '../../core/models/category';
import {Subcategory} from '../../core/models/subcategory';
import {Router} from '@angular/router';
import {CategoryService} from '../../core/services/category/category.service';
import {ConfirmationService, MessageService} from 'primeng/api';
import {SubcategoryService} from '../../core/services/subcategories/subcategory.service';

@Component({
  selector: 'app-subcategory-list-item',
  templateUrl: './subcategory-list-item.component.html',
  styleUrls: ['./subcategory-list-item.component.css']
})
export class SubcategoryListItemComponent implements OnInit {
  @Input()
  subcategory!: Subcategory;
  @Output()
  subcategoryDeleted: EventEmitter<any> = new EventEmitter<any>();

  constructor(private router: Router,
              private subcategoryService: SubcategoryService,
              private messageService: MessageService,
              private confirmationService: ConfirmationService) { }

  ngOnInit(): void {
  }

  onClickDelete(): void{
    this.confirmationService.confirm(
      {
        message: `Are you sure that you want to delete ${this.subcategory?.name} ?`,
        acceptLabel: 'Delete',
        rejectLabel: 'Close',
        header: 'Deletion',
        icon: 'pi pi-trash',
        accept: () => this.deletionConfirmed()
      });
  }

  deletionConfirmed(): void {
    this.subcategoryService.delete(this.subcategory?.id ?? 0).subscribe(() => {
      this.messageService.add({
        severity: 'success',
        summary: 'Deleted successfully',
        detail: 'The subcategory was deleted successfully'
      });
      this.subcategoryDeleted.emit(this.subcategory.id);
    });
  }

  onClickEdit(): void{
    this.router.navigate([`/edit-subcategory/${this.subcategory?.id ?? 0}`]).then(r => console.log('EDIT'));
  }

  onClickSubcategory(): void{
  }
}
