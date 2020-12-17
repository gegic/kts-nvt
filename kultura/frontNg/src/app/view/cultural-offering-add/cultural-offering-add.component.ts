import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {DialogService} from 'primeng/dynamicdialog';
import {CulturalOfferingPlaceComponent} from '../cultural-offering-place/cultural-offering-place.component';
import {NominatimPlace, PlaceOfferingService} from '../../core/services/place-offering/place-offering.service';
import {Place} from '../../core/models/place';
import {BehaviorSubject} from 'rxjs';
import {AutoComplete} from 'primeng/autocomplete';
import {HttpClient} from '@angular/common/http';
import {AddOfferingService} from '../../core/services/add-offering/add-offering.service';
import {Category} from '../../core/models/category';
import {Subcategory} from '../../core/models/subcategory';
import {MessageService} from 'primeng/api';
import {CulturalOfferingPhoto} from '../../core/models/culturalOfferingPhoto';

@Component({
  selector: 'app-cultural-offering-add',
  templateUrl: './cultural-offering-add.component.html',
  styleUrls: ['./cultural-offering-add.component.scss']
})
export class CulturalOfferingAddComponent implements OnInit {

  formGroup: FormGroup = new FormGroup(
    {
      name: new FormControl(''),
      briefInfo: new FormControl('', Validators.maxLength(200)),
      additionalInfo: new FormControl('', Validators.maxLength(1000)),
      address: new FormControl(''),
      selectedSubcategory: new FormControl(),
      selectedCategory: new FormControl()
    }
  );

  mapSet = false;
  categoriesLoading = false;
  subcategoriesLoading = false;
  recommendations: NominatimPlace[] = [];
  lastLoadedPage = {categories: -1, subcategories: -1};
  totalPages = {categories: 0, subcategories: 0};
  fileLoading = false;
  photo: CulturalOfferingPhoto | null = null;

  constructor(private dialogService: DialogService,
              private placeOfferingService: PlaceOfferingService,
              private addOfferingService: AddOfferingService,
              private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.formGroup.get('address')?.valueChanges.subscribe(val => {
        if (val !== this.placeOfferingService.place.getValue().address) {
          this.placeOfferingService.reset();
          this.mapSet = false;
        }
      }
    );
    this.formGroup.get('selectedCategory')?.valueChanges.subscribe((val: Category) => {
      if (!val || !val.id) {
        return;
      }
      this.categoryChosen(val.id);
    });
    this.getCategories();
  }

  showMapDialog(): void {
    this.dialogService.open(CulturalOfferingPlaceComponent, {
      header: 'Choose on map',
      width: '80%',
    }).onClose.subscribe(value => {
      if (!value) {
        return;
      }
      const { address, coordinates } = value;
      this.mapSet = true;
      this.formGroup.patchValue(
        {
          address: {
            display_name: address
          }
        },
        {
          emitEvent: false
        });
      this.addOfferingService.coordinates = coordinates;
    });
  }

  getAddress(event: any): void {
    const enteredAddress = event.query;

    this.placeOfferingService.getRecommendations(enteredAddress).subscribe(
      data => {
        this.recommendations = data as NominatimPlace[];
      }
    );
  }

  addressSelected(place: NominatimPlace): void {
    this.placeOfferingService.recommendationSelected(place.display_name,
      [place.lat, place.lon]);
    this.mapSet = true;
  }

  addressLostFocus(): void {
    if (!this.mapSet) {
      this.formGroup.get('address')?.reset();
      this.mapSet = false;
      this.placeOfferingService.reset();
    }
  }

  getCategories(): void {
    if (this.lastLoadedPage.categories >= this.totalPages.categories) {
      return;
    }
    this.categoriesLoading = true;
    this.addOfferingService.getCategories(this.lastLoadedPage.categories + 1).subscribe(
      data => {
        this.addOfferingService.categories = this.addOfferingService.categories?.concat(data.content as Category[]);
        this.lastLoadedPage.categories = data.pageable.pageNumber;
        this.totalPages.categories = data.totalPages;
        this.categoriesLoading = false;
      }
    );
  }

  categoryChosen(id: number): void {
    this.resetSubcategories();
    this.getSubcategories(id);
  }

  resetSubcategories(): void {
    this.formGroup.get('selectedSubcategory')?.reset();
    this.addOfferingService.subcategories = [];
    this.totalPages.subcategories = 0;
    this.lastLoadedPage.subcategories = -1;
  }

  getSubcategories(passedId?: number): void {
    if (this.lastLoadedPage.subcategories >= this.totalPages.subcategories) {
      return;
    }
    this.subcategoriesLoading = true;
    let id: number;
    if (!passedId) {
      id = (this.formGroup.get('selectedCategory') as Category).id ?? 0;
    } else {
      id = passedId;
    }
    this.addOfferingService.getSubcategories(id, this.lastLoadedPage.subcategories + 1).subscribe(
      data => {
        this.addOfferingService.subcategories = this.addOfferingService.subcategories?.concat(data.content as Subcategory[]);
        this.lastLoadedPage.subcategories = data.pageable.pageNumber;
        this.totalPages.subcategories = data.totalPages;
        this.subcategoriesLoading = false;
      }
    );
  }

  fileChosen(event: any): void {
    const file = event.target.files[0];
    this.fileLoading = true;
    this.addOfferingService.addPhoto(file).subscribe(
      (data: CulturalOfferingPhoto) => {
        this.fileLoading = false;
        this.photo = data;
      },
      err => {
        this.fileLoading = false;
        this.messageService.add({severity: 'error', detail: 'Photo couldn\'t be uploaded due to an unknown reason.'});
      }
    );
  }

  get subcategories(): Subcategory[] {
    return this.addOfferingService.subcategories ?? [];
  }

  get categories(): Category[] {
    return this.addOfferingService.categories ?? [];
  }

  get thumbnailPhoto(): string {
    return `/photos/main/thumbnail/${this.photo?.id}.png`;
  }
}
