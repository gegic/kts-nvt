import {Component, Input, OnInit} from '@angular/core';
import {CulturalOfferingDetailsService} from '../../core/services/cultural-offering-details/cultural-offering-details.service';
import {ActivatedRoute, Router, RouterModule} from '@angular/router';
import {BehaviorSubject} from 'rxjs';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {AuthService} from '../../core/services/auth/auth.service';
import {CulturalOfferingsService} from '../../core/services/cultural-offerings/cultural-offerings.service';
import {ConfirmationService, MessageService} from 'primeng/api';

@Component({
  selector: 'app-cultural-offering-details',
  templateUrl: './cultural-offering-details.component.html',
  styleUrls: ['./cultural-offering-details.component.scss']
})
export class CulturalOfferingDetailsComponent implements OnInit {

  readonly navigationItems = [
    {
      label: 'Posts',
      link: 'posts'
    },
    {
      label: 'Photos',
      link: 'photos'
    },
    {
      label: 'Reviews',
      link: 'reviews'
    },
    {
      label: 'About',
      link: 'about'
    }
  ];

  constructor(private detailsService: CulturalOfferingDetailsService,
              private activatedRoute: ActivatedRoute,
              private authService: AuthService,
              private culturalOfferingsService: CulturalOfferingsService,
              private router: Router,
              private confirmationService: ConfirmationService,
              private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(
      val => {
        this.detailsService.getCulturalOffering(val.id).subscribe(
          data => {
            this.detailsService.culturalOffering.next(data);
          }
        );
      }
    );
  }

  getUserRole(): string {
    return this.authService.getUserRole();
  }

  onClickDelete(): void {
    this.confirmationService.confirm(
      {
        message: `Are you sure that you want to delete ${this.culturalOffering?.name ?? ''}`,
        acceptLabel: 'Delete',
        rejectLabel: 'Close',
        header: 'Deletion',
        icon: 'pi pi-trash',
        accept: () => this.deletionConfirmed()
      });
  }

  onClickEdit(): void {
    this.router.navigate([`/edit-offering/${this.culturalOffering?.id ?? 0}`]);
  }

  deletionConfirmed(): void {
    this.culturalOfferingsService.delete(this.culturalOffering?.id ?? 0).subscribe(() => {
      this.router.navigate(['']);
      this.messageService.add({
        severity: 'success',
        summary: 'Deleted successfully',
        detail: 'The cultural offering was deleted successfully'
      });
    });
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
      return `${this.culturalOffering?.overallRating?.toPrecision(1)} rating out of ${this.culturalOffering?.numReviews} reviews.`;
    }
  }
}
