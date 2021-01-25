import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {DialogService} from 'primeng/dynamicdialog';
import {CulturalOfferingPlaceComponent} from '../cultural-offering-place/cultural-offering-place.component';
import {NominatimPlace, PlaceOfferingService} from '../../core/services/place-offering/place-offering.service';
import {Place} from '../../core/models/place';
import {BehaviorSubject, Subscription} from 'rxjs';
import {AutoComplete} from 'primeng/autocomplete';
import {HttpClient} from '@angular/common/http';
import {AddOfferingService} from '../../core/services/add-offering/add-offering.service';
import {Category} from '../../core/models/category';
import {Subcategory} from '../../core/models/subcategory';
import {MessageService} from 'primeng/api';
import {CulturalOfferingPhoto} from '../../core/models/culturalOfferingPhoto';
import validate = WebAssembly.validate;
import {ActivatedRoute, Router} from '@angular/router';
import {CulturalOffering} from '../../core/models/cultural-offering';

interface Dictionary {
  [key: string]: string;
}

const errorDict: Dictionary = {
  name: 'Name is required and can contain only letters, digits and spaces',
  briefInfo: 'Brief info is required and it can contain a maximum of 200 characters.',
  additionalInfo: 'Additional info can contain 1000 characters at most.',
  address: 'Address is required',
  selectedCategory: 'Category is required.',
  selectedSubcategory: 'Subcategory is required'
};

const idDict: Dictionary = {
  name: 'name-error',
  briefInfo: 'brief-info-error',
  additionalInfo: 'additional-info-error',
  address: 'address-error',
  selectedCategory: 'category-error',
  selectedSubcategory: 'subcategory-error'
};

@Component({
  selector: 'app-cultural-offering-add',
  templateUrl: './cultural-offering-add.component.html',
  styleUrls: ['./cultural-offering-add.component.scss']
})
export class CulturalOfferingAddComponent implements OnInit, OnDestroy {

  private subscriptions: (Subscription | undefined)[] = [];

  formGroup: FormGroup = new FormGroup(
    {
      name: new FormControl(undefined, [Validators.required, Validators.pattern(/[\p{L} \d]+/u)]),
      briefInfo: new FormControl(undefined, [Validators.required, Validators.maxLength(200)]),
      additionalInfo: new FormControl(undefined, Validators.maxLength(1000)),
      address: new FormControl(undefined, Validators.required),
      selectedCategory: new FormControl(undefined, Validators.required),
      selectedSubcategory: new FormControl(undefined, Validators.required)
    }
  );

  mode = 'add';
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
              private messageService: MessageService,
              private router: Router,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.subscriptions.push(
      this.formGroup.get('address')?.valueChanges.subscribe(val => {
          if (val !== this.placeOfferingService.place.getValue().address) {
            this.placeOfferingService.reset();
            this.mapSet = false;
          }
        }
      )
    );
    this.subscriptions.push(
      this.formGroup.get('selectedCategory')?.valueChanges.subscribe((val: Category) => {
        if (!val || !val.id) {
          return;
        }
        this.categoryChosen(val.id);
      })
    );
    this.getCategories();
    this.mode = this.activatedRoute.snapshot.data.mode;
    if (this.mode === 'edit') {
      const id = this.activatedRoute.snapshot.params.id;
      this.subscriptions.push(
        this.addOfferingService.getOffering(id).subscribe((co: CulturalOffering) => {
          this.addOfferingService.culturalOffering = co;
          this.formGroup.patchValue({
            name: co.name,
            briefInfo: co.briefInfo,
            additionalInfo: co.additionalInfo,
            address: {display_name: co.address},
            selectedCategory: {id: co.categoryId, name: co.categoryName},
            selectedSubcategory: {id: co.subcategoryId, name: co.subcategoryName}
          });
          this.formGroup.get('selectedSubcategory')?.disable();
          this.photo = new CulturalOfferingPhoto();
          this.photo.id = co.photoId;
          this.mapSet = true;
        })
      );
    }
  }

  showMapDialog(): void {
    this.subscriptions.push(
      this.dialogService.open(CulturalOfferingPlaceComponent, {
        header: 'Choose on map',
        width: '80%',
      }).onClose.subscribe(value => {
        if (!value) {
          this.placeOfferingService.reset();
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
      })
    );
  }

  getAddress(event: any): void {
    const enteredAddress = event.query;

    this.subscriptions.push(
      this.placeOfferingService.getRecommendations(enteredAddress).subscribe(
        data => {
          this.recommendations = data as NominatimPlace[];
        }
      )
    );
  }

  addressSelected(place: NominatimPlace): void {
    this.placeOfferingService.recommendationSelected(place.display_name,
      [place.lat, place.lon]);
    this.addOfferingService.coordinates = [place.lat, place.lon];
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
    this.subscriptions.push(
      this.addOfferingService.getCategories(this.lastLoadedPage.categories + 1).subscribe(
        data => {
          this.addOfferingService.categories = this.addOfferingService.categories?.concat(data.content as Category[]);
          this.lastLoadedPage.categories = data.pageable.pageNumber;
          this.totalPages.categories = data.totalPages;
          this.categoriesLoading = false;
        }
      )
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
    this.subscriptions.push(
      this.addOfferingService.getSubcategories(id, this.lastLoadedPage.subcategories + 1).subscribe(
        data => {
          this.formGroup.get('selectedSubcategory')?.enable();
          this.addOfferingService.subcategories = this.addOfferingService.subcategories?.concat(data.content as Subcategory[]);
          this.lastLoadedPage.subcategories = data.pageable.pageNumber;
          this.totalPages.subcategories = data.totalPages;
          this.subcategoriesLoading = false;
        }
      )
    );
  }

  fileChosen(event: any): void {
    const file = event.target.files[0];
    this.fileLoading = true;
    this.subscriptions.push(
      this.addOfferingService.addPhoto(file).subscribe(
        (data: CulturalOfferingPhoto) => {
          this.fileLoading = false;
          this.photo = data;
        },
        () => {
          this.fileLoading = false;
          this.messageService.add({severity: 'error', detail: 'Photo couldn\'t be uploaded due to an unknown reason.'});
        }
      )
    );
  }

  saveOffering(): void {
    if (!this.photo) {
      this.messageService.add(
        {
          severity: 'error',
          summary: 'Photo is missing',
          detail: 'A cultural offering has to have a main photo.',
          id: 'photo-error'
        }
      );
      return;
    }
    if (this.formGroup.invalid) {
      for (const c in this.formGroup.controls) {
        if (this.formGroup.controls.hasOwnProperty(c) && this.formGroup.get(c)?.invalid) {
          this.messageService.add(
            {
              severity: 'error',
              summary: 'Offering wasn\'t saved',
              detail: errorDict[c],
              id: idDict[c]
            }
          );
          return;
        }
      }
    }

    this.addOfferingService.name = this.formGroup.get('name')?.value;
    this.addOfferingService.address = this.formGroup.get('address')?.value;
    this.addOfferingService.subcategory = this.formGroup.get('selectedSubcategory')?.value as Subcategory;
    this.addOfferingService.briefInfo = this.formGroup.get('briefInfo')?.value;
    this.addOfferingService.additionalInfo = this.formGroup.get('additionalInfo')?.value;
    this.addOfferingService.photo = this.photo;

    if (this.mode === 'add') {
      this.subscriptions.push(
        this.addOfferingService.addOffering().subscribe(
          data => {
            this.messageService.add({
              severity: 'success',
              summary: 'Successfully added',
              detail: 'The cultural offering was added successfully',
              id: 'offering-added-toast'
            });
            this.router.navigate([`/cultural-offering/${data.id}`]);
          }
        )
      );
    } else {
      this.subscriptions.push(
        this.addOfferingService.editOffering().subscribe(
          data => {
            this.messageService.add({
              severity: 'success',
              summary: 'Successfully edited',
              detail: 'The cultural offering was edited successfully',
              id: 'offering-edited-toast'
            });
            this.router.navigate([`/cultural-offering/${data.id}`]);
          }
        )
      );
    }
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

  ngOnDestroy(): void {
    this.addOfferingService.clearPhotos();
    this.subscriptions.forEach(s => s?.unsubscribe());
  }
}
