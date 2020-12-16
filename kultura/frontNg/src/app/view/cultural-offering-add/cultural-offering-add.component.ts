import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-cultural-offering-add',
  templateUrl: './cultural-offering-add.component.html',
  styleUrls: ['./cultural-offering-add.component.scss']
})
export class CulturalOfferingAddComponent implements OnInit {

  formGroup: FormGroup = new FormGroup(
    {
      name: new FormControl(''),
      categoryName: new FormControl(''),
      subcategoryName: new FormControl(''),
      briefInfo: new FormControl('', Validators.maxLength(200)),
      additionalInfo: new FormControl('', Validators.maxLength(1000))
    }
  );
  subcategoryName = '';

  constructor() {
  }

  ngOnInit(): void {
  }

  searchCategories(event: Event): void {
  }

  get selectedCategory(): any {
    return '';
  }

  get categorySuggestions(): string[] {
    return ['dsa', 'dsa', 'dsa'];
  }

  searchSubcategories(event: Event): void {
  }

  get selectedSubcategory(): any {
    return '';
  }

  get subcategorySuggestions(): string[] {
    return ['dsa', 'dsa', 'dsa'];
  }
}
