import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {Review} from '../../core/models/review';
import {TimeUtil} from '../../core/timeUtil';
import * as moment from 'moment-timezone';
import {Confirmation, ConfirmationService, MenuItem} from 'primeng/api';
import {ReviewService} from '../../core/services/review/review.service';
import {AuthService} from '../../core/services/auth/auth.service';
import {ReviewPhoto} from '../../core/models/reviewPhoto';
import {ReviewGalleriaService} from '../../core/services/review-galleria/review-galleria.service';
import {ReviewNumbers} from '../../core/models/reviewNumbers';
import {Subscription} from 'rxjs';
import {Menu} from 'primeng/menu';

@Component({
  selector: 'app-review-element',
  templateUrl: './review-element.component.html',
  styleUrls: ['./review-element.component.scss']
})
export class ReviewElementComponent implements OnDestroy {

  private subscriptions: Subscription[] = [];

  @Input()
  review?: Review;
  @Output()
  reviewDeleted: EventEmitter<any> = new EventEmitter<any>();

  menuItems: MenuItem[] = [{ label: 'Delete', command: () => this.deleteReview() }];

  constructor(private confirmationService: ConfirmationService,
              private reviewService: ReviewService,
              private authService: AuthService,
              private reviewGalleriaService: ReviewGalleriaService) {
  }

  getUserRole(): string {
    return this.authService.getUserRole();
  }

  deleteReview(): void {
    this.confirmationService.confirm(
      {
        message: 'Are you sure that you want to delete this review',
        acceptLabel: 'Delete',
        rejectLabel: 'Close',
        header: 'Deletion',
        icon: 'pi pi-trash',
        accept: () => this.reviewDeletionConfirmed()
      });
  }

  reviewDeletionConfirmed(): void {
    if (!this.review || !this.review.id) {
      return;
    }
    this.subscriptions.push(
      this.reviewService.delete(this.review.id).subscribe(() => {
        this.reviewDeleted.emit();
      })
    );
  }

  getAddedAgoString(): string {
    if (!this.review?.timeAdded) {
      return 'Some time ago';
    }
    const now = moment();
    const timeAddedMs = moment.utc(this.review.timeAdded);
    return TimeUtil.timeDifference(now.valueOf(), timeAddedMs.valueOf());
  }

  getThumbnailUrl(photoId: number): string {
    return `/photos/review/thumbnail/${photoId}.png`;
  }

  onClickReviewImg(index: number): void {
    this.reviewGalleriaService.value = this.photos;
    this.reviewGalleriaService.activeIndex = index;
    this.reviewGalleriaService.visible = true;
  }

  get photos(): number[] {
    return this.review?.photos ?? [];
  }

  ngOnDestroy(): void {
    this.reviewGalleriaService.value = [];
    this.reviewGalleriaService.visible = false;
    this.reviewGalleriaService.activeIndex = 0;
    this.subscriptions.forEach(s => s.unsubscribe());
  }
}
