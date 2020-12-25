import { Component, OnInit, Input } from '@angular/core';
import { ReviewService } from 'src/app/core/services/review/review.service';
@Component({
  selector: 'app-offering-rating',
  templateUrl: './offering-rating.component.html',
  styleUrls: ['./offering-rating.component.css']
})
export class OfferingRatingComponent implements OnInit {

  @Input() offering_id:Number = NaN;

  rating:Number = 0;

  hovered:Number = 0;

  ratingSize:Number[] = new Array(5);

  ratings:Object[] = [];

  reviewService:ReviewService;

  constructor(rService:ReviewService) {
    this.reviewService = rService;
    this.ratings = this.reviewService.getReviews(this.offering_id);
   }
  ngOnInit(): void {
  }

  review(){

  }

}
