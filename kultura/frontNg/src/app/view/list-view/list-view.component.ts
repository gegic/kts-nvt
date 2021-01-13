import {Component, OnDestroy, OnInit} from '@angular/core';
import {CulturalOfferingsService} from '../../core/services/cultural-offerings/cultural-offerings.service';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {MenuItem} from 'primeng/api';
import {Category} from '../../core/models/category';
import {Subcategory} from '../../core/models/subcategory';
import * as L from 'leaflet';
import {NominatimPlace} from '../../core/services/place-offering/place-offering.service';

@Component({
  selector: 'app-list-view',
  templateUrl: './list-view.component.html',
  styleUrls: ['./list-view.component.scss']
})
export class ListViewComponent implements OnInit, OnDestroy {

  page = -1;
  totalPages = 0;
  sortType = {label: 'id (ascending)', sort: 'id,asc'};
  sortItems: MenuItem[] = [
    {label: 'id (ascending)', command: () => this.setSortType('id (ascending)', 'id,asc')},
    {label: 'id (descending)', command: () => this.setSortType('id (descending)', 'id,desc')},
    {label: 'name (ascending)', command: () => this.setSortType('name (ascending)', 'name,asc')},
    {label: 'name (descending)', command: () => this.setSortType('name (descending)', 'name,desc')},
    {label: 'reviews (ascending)', command: () => this.setSortType('reviews (ascending)', 'numReviews,asc')},
    {label: 'reviews (descending)', command: () => this.setSortType('reviews (descending)', 'numReviews,desc')},
    {label: 'rating (ascending)', command: () => this.setSortType('rating (ascending)', 'overallRating,asc')},
    {label: 'rating (descending)', command: () => this.setSortType('rating (descending)', 'overallRating,desc')},
  ];
  isFilterDialogOpen = false;

  filter: {
    rating: any[],
    category?: Category,
    subcategory?: Subcategory,
    noReviews: boolean
  } = {
    rating: [1, 5],
    noReviews: true
  };
  relativeLocation?: [number, number];
  absoluteLocation?: [number, number];
  lastLoadedPageFilter = {categories: -1, subcategories: -1};
  totalPagesFilter = {categories: 0, subcategories: 0};
  categoriesLoading = false;
  subcategoriesLoading = false;
  filterSet = false;
  isLocationRelative = false;
  filterByLocation = false;
  locationDistance = 2;
  private map: L.Map | null = null;
  recommendations: NominatimPlace[] = [];
  address = '';


  constructor(private culturalOfferingsService: CulturalOfferingsService) { }

  ngOnInit(): void {
    this.culturalOfferingsService.searchQuery.subscribe(val => {
      this.resetCulturalOfferings();
    });
    this.getCulturalOfferings();
    this.getLocation();
  }

  getLocation(): void {
    navigator.geolocation.getCurrentPosition(position => {
      this.relativeLocation = [position.coords.latitude, position.coords.longitude];
    });
  }

  getCulturalOfferings(): void {
    if (this.page === this.totalPages) {
      return;
    }
    this.culturalOfferingsService.getCulturalOfferings(this.page + 1, this.sortType.sort).subscribe(
      val => {
        for (const el of val.content) {
          if (this.culturalOfferingsService.culturalOfferings.some(co => co.id === el.id)) {
            continue;
          }
          this.culturalOfferingsService.culturalOfferings.push(el);
        }
        this.page = val.pageable.pageNumber;
        this.totalPages = val.totalPages;
      }
    );
  }

  resetCulturalOfferings(): void {
    this.culturalOfferingsService.culturalOfferings = [];
    this.page = -1;
    this.totalPages = 0;
    this.getCulturalOfferings();
  }

  onScrollDown(): void {
    this.getCulturalOfferings();
  }

  onOfferingDeleted(): void {
    this.resetCulturalOfferings();
  }

  setSortType(label: string, sort: string): void {
    this.sortType.label = label;
    this.sortType.sort = sort;
    this.resetCulturalOfferings();
  }

  resetFilter(): void {
    this.filterSet = false;
    this.filter.rating = [0, 5];
    this.filter.noReviews = true;
    this.filter.category = undefined;
    this.filter.subcategory = undefined;
    this.filterByLocation = false;
    this.isLocationRelative = false;
    this.locationDistance = 2;
    this.saveFilter(true);
  }

  restoreFilter(): void {
    this.filter.rating = [this.culturalOfferingsService.rating.min, this.culturalOfferingsService.rating.max];
    this.filter.noReviews = this.culturalOfferingsService.noReviews;
    this.filter.category = this.culturalOfferingsService.selectedCategory;
    this.filter.subcategory = this.culturalOfferingsService.selectedSubcategory;
    this.filterByLocation = this.culturalOfferingsService.filterByLocation;
    this.isLocationRelative = this.culturalOfferingsService.isLocationRelative;
    this.locationDistance = this.culturalOfferingsService.locationDistance;
  }

  saveFilter(reset?: boolean): void {
    if (!reset) {
      this.filterSet = true;
    }
    this.culturalOfferingsService.rating = {
      min: this.filter.rating[0], max: this.filter.rating[1]
    };
    this.culturalOfferingsService.noReviews = this.filter.noReviews;
    this.culturalOfferingsService.selectedCategory = this.filter.category;
    this.culturalOfferingsService.selectedSubcategory = this.filter.subcategory;
    this.culturalOfferingsService.filterByLocation = this.filterByLocation;
    this.culturalOfferingsService.isLocationRelative = this.isLocationRelative;
    this.culturalOfferingsService.locationDistance = this.locationDistance;
    let bounds: L.LatLngBounds | undefined;
    if (this.isLocationRelative && this.relativeLocation) {
      bounds = (new L.LatLng(this.relativeLocation[0] ?? 0, this.relativeLocation[1] ?? 0))
        .toBounds(this.locationDistance * 1000);
    } else if (this.absoluteLocation) {
      bounds = (new L.LatLng(this.absoluteLocation[0] ?? 0, this.absoluteLocation[1] ?? 0))
        .toBounds(this.locationDistance * 1000);
    }
    if (!!bounds) {
      const southWest = bounds.getSouthWest();
      const northEast = bounds.getNorthEast();
      this.culturalOfferingsService.latitudeStart = southWest.lat;
      this.culturalOfferingsService.longitudeStart = southWest.lng;
      this.culturalOfferingsService.latitudeEnd = northEast.lat;
      this.culturalOfferingsService.longitudeEnd = northEast.lng;
    } else {
      this.culturalOfferingsService.latitudeStart = undefined;
      this.culturalOfferingsService.longitudeStart = undefined;
      this.culturalOfferingsService.latitudeEnd = undefined;
      this.culturalOfferingsService.longitudeEnd = undefined;
    }
    this.resetCulturalOfferings();
    this.isFilterDialogOpen = false;
  }

  getCategories(reset?: boolean): void {
    if (reset) {
      this.lastLoadedPageFilter.categories = -1;
      this.totalPagesFilter.categories = 0;
    }
    if (this.lastLoadedPageFilter.categories >= this.totalPagesFilter.categories) {
      return;
    }
    this.categoriesLoading = true;
    this.culturalOfferingsService.getCategories(this.lastLoadedPageFilter.categories + 1).subscribe(
      data => {
        this.culturalOfferingsService.categories = this.culturalOfferingsService.categories?.concat(data.content as Category[]);
        this.lastLoadedPageFilter.categories = data.pageable.pageNumber;
        this.totalPagesFilter.categories = data.totalPages;
        this.categoriesLoading = false;
      }
    );
  }

  getSubcategories(passedId?: number): void {
    if (this.lastLoadedPageFilter.subcategories >= this.totalPagesFilter.subcategories) {
      return;
    }
    this.subcategoriesLoading = true;
    let id: number;
    if (!passedId) {
      id = this.filter.category?.id ?? 0;
    } else {
      id = passedId;
    }
    this.culturalOfferingsService.getSubcategories(id, this.lastLoadedPageFilter.subcategories + 1).subscribe(
      data => {
        this.culturalOfferingsService.subcategories = this.culturalOfferingsService
          .subcategories?.concat(data.content as Subcategory[]);
        this.lastLoadedPageFilter.subcategories = data.pageable.pageNumber;
        this.totalPagesFilter.subcategories = data.totalPages;
        this.subcategoriesLoading = false;
      }
    );
  }

  resetSubcategories(): void {
    this.filter.subcategory = undefined;
    this.culturalOfferingsService.subcategories = [];
    this.totalPagesFilter.subcategories = 0;
    this.lastLoadedPageFilter.subcategories = -1;
  }

  openFilterDialog(): void {
    this.isFilterDialogOpen = true;
    this.getCategories(true);
  }

  getAddress(event: any): void {
    const enteredAddress = event.query;

    this.culturalOfferingsService.getRecommendations(enteredAddress).subscribe(
      data => {
        this.recommendations = data as NominatimPlace[];
      }
    );
  }

  addressSelected(place: NominatimPlace): void {
    this.absoluteLocation = [place.lat, place.lon];
  }

  addressLostFocus(): void {
    this.address = '';
    this.absoluteLocation = undefined;
  }

  categoryChosen(id: number): void {
    this.resetSubcategories();
    this.getSubcategories(id);
  }

  get culturalOfferings(): CulturalOffering[] {
    return this.culturalOfferingsService.culturalOfferings;
  }

  get onlyReviews(): boolean {
    return !this.filter.noReviews;
  }

  set onlyReviews(val: boolean) {
    this.filter.noReviews = !val;
  }

  get subcategories(): Subcategory[] {
    return this.culturalOfferingsService.subcategories ?? [];
  }

  get categories(): Category[] {
    return this.culturalOfferingsService.categories ?? [];
  }

  ngOnDestroy(): void {
    this.culturalOfferingsService.culturalOfferings = [];
  }
}
