import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {DialogService} from 'primeng/dynamicdialog';
import {CulturalOfferingPlaceComponent} from '../cultural-offering-place/cultural-offering-place.component';
import {NominatimPlace, PlaceOfferingService} from '../../core/services/place-offering/place-offering.service';
import {Place} from '../../core/models/place';
import {BehaviorSubject} from 'rxjs';
import {AutoComplete} from 'primeng/autocomplete';

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
      subcategoryName: new FormControl({ value: '', disabled: true }),
      briefInfo: new FormControl('', Validators.maxLength(200)),
      additionalInfo: new FormControl('', Validators.maxLength(1000)),
      address: new FormControl('')
    }
  );

  coordinates: BehaviorSubject<number[] | null> = new BehaviorSubject<number[] | null>(null);
  mapSet = false;
  recommendations: NominatimPlace[] = [];

  constructor(private dialogService: DialogService,
              private placeOfferingService: PlaceOfferingService) {
    this.formGroup.get('address')?.valueChanges.subscribe(val => {
        if (val !== this.placeOfferingService.place.getValue().address) {
          this.placeOfferingService.reset();
          this.mapSet = false;
        }
      }
    );
  }

  ngOnInit(): void {
  }

  searchCategories(event: Event): void {
  }

  searchSubcategories(event: Event): void {
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
      this.coordinates.next(coordinates);
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

  get selectedCategory(): any {
    return '';
  }

  get categorySuggestions(): string[] {
    return ['dsa', 'dsa', 'dsa'];
  }


  get selectedSubcategory(): any {
    return '';
  }

  get subcategorySuggestions(): string[] {
    return ['dsa', 'dsa', 'dsa'];
  }

  get recommendationStrings(): string[] {
    return this.recommendations.map(r => r.display_name);
  }

}
