import { Component, OnInit, Input } from '@angular/core';
import { ReviewService } from 'src/app/core/services/review/review.service';
@Component({
  selector: 'app-offering-rating',
  templateUrl: './offering-review.component.html',
  styleUrls: ['./offering-review.component.css']
})
export class OfferingReviewComponent implements OnInit {


  @Input()cultural_offering:any = {};


  review={
    rating:0,
    comment:"",
    timeAdded:null,
    culturalOfferingId:this.cultural_offering.id,
  };


  hovered:Number = 0;

  ratingSize:Number[] = new Array(5);

  ratings:Object[] = [];

  reviewService:ReviewService;

  constructor(rService:ReviewService) {
    this.reviewService = rService;
   }
  ngOnInit(): void {
    this.ratings = this.reviewService.getReviews(this.cultural_offering.id);
  }

  isCovered(i:number){
    return i<this.hovered||i<this.review.rating;
  }

}
