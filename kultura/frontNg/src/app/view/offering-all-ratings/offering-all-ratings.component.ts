import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-offering-all-ratings',
  templateUrl: './offering-all-ratings.component.html',
  styleUrls: ['./offering-all-ratings.component.css']
})
export class OfferingAllRatingsComponent implements OnInit {
  data: any;
  ratingSize:Number[] = new Array(5);

  rating:Number = 3.1;

  numRating:Number = 333;

  constructor() {
    this.data = {
      labels: ['5', '4', '3', '2', '1'],
      datasets: [
        {
          data: [300, 150, 100, 120, 140],
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
      if(this.rating<1.5){
        return "#2f964a";
      }
      if(this.rating<2.5){
        return "#dd780c";
      }
      if(this.rating<3.5){
        return "#d8dd0c";
      }
      if(this.rating<4.5){
        return "#4eca1c";
      }
      if(this.rating<=5){
        return "#2f964a";
      }
      return ""
  }
}
