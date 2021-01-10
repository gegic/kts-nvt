import {Component, OnDestroy, OnInit} from '@angular/core';
import {CulturalOfferingDetailsService} from '../../core/services/cultural-offering-details/cultural-offering-details.service';
import {PostsService} from '../../core/services/posts/posts.service';
import {ConfirmationService, MessageService} from 'primeng/api';
import {Router} from '@angular/router';
import {DialogService} from 'primeng/dynamicdialog';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {ReviewService} from '../../core/services/review/review.service';
import {ReviewNumbers} from '../../core/models/reviewNumbers';
import {Review} from '../../core/models/review';
import {AuthService} from '../../core/services/auth/auth.service';
import {ReviewPhoto} from '../../core/models/reviewPhoto';
import {CulturalOfferingPhoto} from '../../core/models/culturalOfferingPhoto';
import {ReviewGalleriaService} from '../../core/services/review-galleria/review-galleria.service';

@Component({
  selector: 'app-reviews',
  templateUrl: './reviews.component.html',
  styleUrls: ['./reviews.component.scss']
})
export class ReviewsComponent implements OnInit, OnDestroy {

  page = -1;
  totalPages = 0;
  isAddDialogOpen = false;
  userReview = new Review();
  uploadLoading = false;

  constructor(private detailsService: CulturalOfferingDetailsService,
              private reviewService: ReviewService,
              private messageService: MessageService,
              private router: Router,
              private confirmationService: ConfirmationService,
              private dialogService: DialogService,
              private authService: AuthService,
              private reviewGalleriaService: ReviewGalleriaService) { }

  ngOnInit(): void {
    this.detailsService.culturalOffering.subscribe(val => {
      if (!!val) {
        this.reviewService.reviews = [];
        this.reviewService.reviewNumbers = new ReviewNumbers();
        this.getReviews();
      }
    });
  }

  getReviews(): void {
    if (!this.detailsService.culturalOffering.getValue()) {
      return;
    }
    if (this.page === this.totalPages) {
      return;
    }
    this.reviewService.getReviews(this.culturalOffering?.id ?? 0, this.page + 1).subscribe(val => {
      for (const el of val.content) {
        if (this.reviewService.reviews.some(re => re.id === el.id)) {
          continue;
        }
        const id = this.authService.user.getValue()?.id;
        if (id && el.userId === this.authService.user.getValue()?.id) {
          continue;
        }
        this.reviewService.reviews.push(el);
      }
      this.page = val.pageable.pageNumber;
      this.totalPages = val.totalPages;
    });
    this.reviewService.getReviewNumbers(this.culturalOffering?.id ?? 0).subscribe(val => {
      for (const reviewNum of val) {
        switch (reviewNum.rating) {
          case 1:
            this.reviewService.reviewNumbers.oneStar = reviewNum.numReviews;
            break;
          case 2:
            this.reviewService.reviewNumbers.twoStars = reviewNum.numReviews;
            break;
          case 3:
            this.reviewService.reviewNumbers.threeStars = reviewNum.numReviews;
            break;
          case 4:
            this.reviewService.reviewNumbers.fourStars = reviewNum.numReviews;
            break;
          case 5:
            this.reviewService.reviewNumbers.fiveStars = reviewNum.numReviews;
            break;
          default:
        }
      }
    });

    this.getReviewForUser();
  }

  getReviewForUser(): void {
    const culturalOfferingId = this.culturalOffering?.id;
    const userId = this.authService.user.getValue()?.id;
    if (!culturalOfferingId || !userId) {
      return;
    }
    this.reviewService.getReviewForUser(culturalOfferingId, userId).subscribe(val => {
      this.userReview = val;
    });
  }

  onScrollDown(): void {
    this.getReviews();
  }

  onReviewDeleted(): void {
    this.messageService.add({
      severity: 'success',
      summary: 'Review deleted',
      detail: 'The review was deleted successfully.'
    });
    this.resetOffering();
    this.resetReviews();
  }

  resetReviews(): void {
    this.reviewService.reviews = [];
    this.reviewService.reviewNumbers = new ReviewNumbers();
    this.page = -1;
    this.totalPages = 0;
    this.getReviews();
  }

  clearReviewToAdd(): void {
    if (!this.userReview.id) {
      this.userReview = new Review();
    } else {
      this.getReviewForUser();
    }
    this.reviewService.clearPhotos().subscribe();
  }

  getReviewNumberPercentage(numReviews: number): number {
    return numReviews / (this.culturalOffering?.numReviews ?? 1) * 100;
  }

  getUserRole(): string {
    return this.authService.getUserRole();
  }

  resetOffering(): void {
    this.detailsService.getCulturalOffering(this.culturalOffering?.id ?? 0).subscribe(
      val => {
        this.detailsService.culturalOffering.next(val);
      }
    );
  }

  onWriteReviewClick(): void {
    this.isAddDialogOpen = true;
  }

  onUploadStart(event: any): void {
    if (event.files.length > 3) {
      this.messageService.add({
        severity: 'error',
        summary: 'Three photos',
        detail: 'You cannot upload more than three photos.'
      });
      return;
    }
    this.uploadLoading = true;
    this.reviewService.addPhotos(event.files).subscribe(val => {
      this.uploadLoading = false;
      this.userReview.photos = val.map((p: ReviewPhoto) => p.id);
      this.messageService.add({
        severity: 'success',
        summary: 'Successful',
        detail: 'Photos uploaded successfully'
      });
    });
  }

  onClickReupload(): void {
    if (!this.userReview.id) {
      this.reviewService.clearPhotos().subscribe(() => {
        this.userReview.photos = undefined;
      });
    } else {
      const culturalOfferingId = this.culturalOffering?.id;
      const userId = this.authService.user.getValue()?.id;
      if (!culturalOfferingId || !userId) {
        this.messageService.add({
          severity: 'error',
          summary: 'Unexpected error',
          detail: 'An unexpected error occurred.'
        });
        return;
      }
      this.reviewService.deletePhotosByCulturalOfferingAndUser(this.userReview.id).subscribe(() => {
        this.userReview.photos = undefined;
      });
    }
  }

  onClickSend(): void {
    if (!this.userReview.rating || this.userReview.rating < 1 || this.userReview.rating > 5) {
      this.messageService.add({
        severity: 'error',
        summary: 'Rating is missing',
        detail: 'Review rating is required, whereas other fields are optional.'
      });
      return;
    }

    this.userReview.userId = this.authService.user.getValue()?.id;
    this.userReview.culturalOfferingId = this.detailsService.culturalOffering.getValue()?.id;

    if (!this.userReview.userId || !this.userReview.culturalOfferingId) {
      this.messageService.add({
        severity: 'error',
        summary: 'Unexpected error',
        detail: 'An unexpected error occurred. Please refresh the page.'
      });
      return;
    }
    if (!this.userReview.id) {
      this.reviewService.add(this.userReview).subscribe(addedReview => {
        this.resetOffering();
        this.resetReviews();
        this.userReview = addedReview;
        this.isAddDialogOpen = false;
      });
    } else {
      this.reviewService.edit(this.userReview).subscribe(editedReview => {
        this.resetOffering();
        this.resetReviews();
        this.userReview = editedReview;
        this.isAddDialogOpen = false;
      });
    }
  }

  getThumbnailUrl(photoId: number): string {
    return `/photos/review/thumbnail/${photoId}.png`;
  }

  getPhotoUrl(photoId: number): string {
    return `/photos/review/${photoId}.png`;
  }


  get culturalOffering(): CulturalOffering | undefined {
    return this.detailsService.culturalOffering.getValue();
  }

  get overallRating(): string {
    return (Math.round((this.culturalOffering?.overallRating ?? 0) * 10) / 10).toFixed(1);
  }

  get reviewNumbers(): ReviewNumbers {
    return this.reviewService.reviewNumbers;
  }

  get reviews(): Review[] {
    return this.reviewService.reviews;
  }

  get galleriaVisible(): boolean {
    return this.reviewGalleriaService.visible;
  }

  set galleriaVisible(val: boolean) {
    this.reviewGalleriaService.visible = val;
  }

  get galleriaValue(): any[] {
    return this.reviewGalleriaService.value ?? [];
  }

  get galleriaActiveIndex(): number {
    return this.reviewGalleriaService.activeIndex;
  }

  set galleriaActiveIndex(val: number) {
    this.reviewGalleriaService.activeIndex = val;
  }

  ngOnDestroy(): void {
    this.reviewService.clearPhotos().subscribe();
  }
}
