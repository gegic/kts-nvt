import {Component, Input, OnInit} from '@angular/core';
import { ReviewService } from 'src/app/core/services/review/review.service';

@Component({
  selector: 'app-offering-all-ratings',
  templateUrl: './offering-all-ratings.component.html',
  styleUrls: ['./offering-all-ratings.component.css']
})
export class OfferingAllRatingsComponent implements OnInit {

  @Input() id:Number = NaN;

  reviewService:ReviewService;


  data: any;
  ratingSize:Number[] = new Array(5);

  summary:any;

  constructor(reviewService:ReviewService) {

    this.summary = reviewService.getSummary(this.id);

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

  ngOnInit(): void {
  }


  resolveColor(){
      if(this.summary.rating<1.5){
        return "#2f964a";
      }
      if(this.summary.rating<2.5){
        return "#dd780c";
      }
      if(this.summary.rating<3.5){
        return "#d8dd0c";
      }
      if(this.summary.rating<4.5){
        return "#4eca1c";
      }
      if(this.summary.rating<=5){
        return "#2f964a";
      }
      return ""
  }
}
