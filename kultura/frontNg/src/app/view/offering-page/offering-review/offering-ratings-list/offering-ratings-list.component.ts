import { Component, OnInit, Input, Inject } from '@angular/core';
import { ReviewService } from 'src/app/core/services/review/review.service';

@Component({
  selector: 'app-offering-ratings-list',
  templateUrl: './offering-ratings-list.component.html',
  styleUrls: ['./offering-ratings-list.component.css']
})
export class OfferingRatingsListComponent implements OnInit {
  @Input() ratings:Object[] = [];

  constructor(){
    
  }

  ngOnInit(): void {
  }

}
