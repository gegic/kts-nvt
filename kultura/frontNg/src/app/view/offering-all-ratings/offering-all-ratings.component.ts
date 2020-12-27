import {Component, Input, OnInit} from '@angular/core';
import { ReviewService } from 'src/app/core/services/review/review.service';

@Component({
  selector: 'app-offering-all-ratings',
  templateUrl: './offering-all-ratings.component.html',
  styleUrls: ['./offering-all-ratings.component.css']
})
export class OfferingAllRatingsComponent implements OnInit {

  @Input() cultural_offering:any;

  reviewService:ReviewService;


  data: any;
  ratingSize:Number[] = new Array(5);

  summary:any;

  constructor(reviewService:ReviewService) {
    this.reviewService = reviewService;
  }

  ngOnInit(): void {
    this.summary = this.reviewService.getSummary(this.cultural_offering.id);
    let labels = [];
    let data = [];
    for(let group in this.summary.ratings){
      labels.push(group);
    }
    labels.sort((a, b)=>b-a);
    for(let l of labels){
      data.push(this.summary.ratings[l]);
    }
    this.data = {
      labels: labels,
      datasets: [
        {
          data: data,
          backgroundColor: [
            "#2f964a",
            "#4eca1c",
            "#d8dd0c",
            "#dd780c",
            "#dd2b0c",
          ],
          hoverBackgroundColor: [
            "#2f964a",
            "#4eca1c",
            "#d8dd0c",
            "#dd780c",
            "#dd2b0c",
          ]
        }
      ]
    };
  }


  resolveColor(){
      if(this.cultural_offering.overallRating<1.5){
        return "#2f964a";
      }
      if(this.cultural_offering.overallRating<2.5){
        return "#dd780c";
      }
      if(this.cultural_offering.overallRating<3.5){
        return "#d8dd0c";
      }
      if(this.cultural_offering.overallRating<4.5){
        return "#4eca1c";
      }
      if(this.cultural_offering.overallRating<=5){
        return "#2f964a";
      }
      return ""
  }
}
