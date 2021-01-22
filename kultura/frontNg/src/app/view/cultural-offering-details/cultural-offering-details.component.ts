import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {CulturalOfferingDetailsService} from '../../core/services/cultural-offering-details/cultural-offering-details.service';
import {ActivatedRoute, Router, RouterModule} from '@angular/router';
import {BehaviorSubject, Subscription} from 'rxjs';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {AuthService} from '../../core/services/auth/auth.service';
import {CulturalOfferingsService} from '../../core/services/cultural-offerings/cultural-offerings.service';
import {ConfirmationService, MessageService} from 'primeng/api';
import {distinctUntilChanged} from 'rxjs/operators';

@Component({
  selector: 'app-cultural-offering-details',
  templateUrl: './cultural-offering-details.component.html',
  styleUrls: ['./cultural-offering-details.component.scss']
})
export class CulturalOfferingDetailsComponent implements OnInit, OnDestroy {

  readonly navigationItems = [
    {
      label: 'Posts',
      link: 'posts',
      icon: 'pi pi-bars'
    },
    {
      label: 'Photos',
      link: 'photos',
      icon: 'pi pi-images'
    },
    {
      label: 'Reviews',
      link: 'reviews',
      icon: 'pi pi-star'
    },
    {
      label: 'About',
      link: 'about',
      icon: 'pi pi-info-circle'
    }
  ];

  private subscriptions: Subscription[] = [];

  isOfferingLoading = false;

  constructor(private detailsService: CulturalOfferingDetailsService,
              private activatedRoute: ActivatedRoute,
              private authService: AuthService,
              private culturalOfferingsService: CulturalOfferingsService,
              private router: Router,
              private confirmationService: ConfirmationService,
              private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.subscriptions.push(this.activatedRoute.params.pipe(distinctUntilChanged()).subscribe(
      val => {
        this.getCulturalOffering(val.id);
      }
    ));
  }

  getUserRole(): string {
    return this.authService.getUserRole();
  }

  getCulturalOffering(id: number): void {
    let userId: number | undefined;
    if (this.isLoggedIn() && this.getUserRole() === 'USER') {
      userId = this.authService.user.getValue()?.id ?? -1;
    }
    this.isOfferingLoading = true;
    this.detailsService.getCulturalOffering(id, userId).pipe(distinctUntilChanged()).subscribe(
      data => {
        this.detailsService.culturalOffering.next(data);
        this.isOfferingLoading = false;
      }
    );
  }

  onClickDelete(): void {
    this.confirmationService.confirm(
      {
        message: `Are you sure that you want to delete ${this.culturalOffering?.name ?? ''}`,
        acceptLabel: 'Delete',
        rejectLabel: 'Close',
        header: 'Deletion',
        icon: 'pi pi-trash',
        acceptButtonStyleClass: 'confirm-deletion',
        accept: () => this.deletionConfirmed()
      });
  }

  onClickEdit(): void {
    this.router.navigate([`/edit-offering/${this.culturalOffering?.id ?? 0}`]);
  }


  onClickSubscribe(): void {
    if (!this.isLoggedIn()) {
      this.router.navigate(['login']);
    } else {
      this.subscriptions.push(this.culturalOfferingsService.subscribe(this.authService.user.getValue()?.id ?? 0,
        this.culturalOffering?.id ?? 0).subscribe(data => {
          this.detailsService.culturalOffering.next(data);
      }));
    }
  }

  onClickUnsubscribe(): void {
    this.subscriptions.push(this.culturalOfferingsService.unsubscribe(this.authService.user.getValue()?.id ?? 0,
      this.culturalOffering?.id ?? 0).subscribe(data => {
        this.detailsService.culturalOffering.next(data);
      }));
  }


  deletionConfirmed(): void {
    this.subscriptions.push(this.culturalOfferingsService.delete(this.culturalOffering?.id ?? 0).subscribe(() => {
      this.router.navigate(['']);
      this.messageService.add({
        severity: 'success',
        summary: 'Deleted successfully',
        detail: 'The cultural offering was deleted successfully',
        id: 'deletion-successful'
      });
    }));
  }

  onClickViewMap(): void {
    this.router.navigate([''],
      {queryParams: {lat: this.culturalOffering?.latitude ?? 0, lng: this.culturalOffering?.longitude ?? 0}});
  }

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }
  get culturalOffering(): CulturalOffering | undefined {
    return this.detailsService.culturalOffering.getValue();
  }

  get photoUrl(): string {
    return `/photos/main/thumbnail/${this.culturalOffering?.photoId ?? -1}.png`;
  }

  get reviews(): string {
    if (this.culturalOffering?.numReviews === 0) {
      return 'No reviews so far.';
    } else {
      return `${(Math.round((this.culturalOffering?.overallRating ?? 0) * 10) / 10).toFixed(1)} rating out of ${this.culturalOffering?.numReviews} reviews.`;
    }
  }

  ngOnDestroy(): void {
    this.detailsService.culturalOffering.next(undefined);
    this.subscriptions.forEach(s => s.unsubscribe());
  }
}
