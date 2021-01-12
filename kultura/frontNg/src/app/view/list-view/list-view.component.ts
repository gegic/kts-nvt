import {Component, OnDestroy, OnInit} from '@angular/core';
import {CulturalOfferingsService} from '../../core/services/cultural-offerings/cultural-offerings.service';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {MenuItem} from 'primeng/api';

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
  isOpenRatingDialog = false;
  isOpenReviewsDialog = false;
  isOpenCategoryDialog = false;

  filter: {
    rating?: any[],
    reviews?: number,
    category?: number
  } = {};

  constructor(private culturalOfferingsService: CulturalOfferingsService) { }

  ngOnInit(): void {
    this.culturalOfferingsService.searchQuery.subscribe(val => {
      this.resetCulturalOfferings();
    });
    this.getCulturalOfferings();
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

  resetRating(): void {
    if (!this.culturalOfferingsService.rating) {
      this.filter.rating = undefined;
    } else {
      this.filter.rating = [this.culturalOfferingsService.rating.min, this.culturalOfferingsService.rating.max];
    }
  }

  saveRating(): void {
    const rating = this.filter.rating ?? [1, 5];
    this.culturalOfferingsService.rating = {
      min: rating[0], max: rating[1]
    };
    this.resetCulturalOfferings();
  }

  get culturalOfferings(): CulturalOffering[] {
    return this.culturalOfferingsService.culturalOfferings;
  }

  ngOnDestroy(): void {
    this.culturalOfferingsService.culturalOfferings = [];
  }
}
