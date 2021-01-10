import {Component, Input, OnInit} from '@angular/core';
import {CulturalOfferingDetailsService} from '../../core/services/cultural-offering-details/cultural-offering-details.service';
import {ActivatedRoute} from '@angular/router';
import {BehaviorSubject} from 'rxjs';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {AuthService} from '../../core/services/auth/auth.service';

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
              private authService: AuthService) {
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
