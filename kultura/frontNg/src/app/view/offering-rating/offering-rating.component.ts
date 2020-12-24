import { Component, OnInit, Input } from '@angular/core';
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

  constructor() { }

  ngOnInit(): void {
  }

  review(){

  }

}
