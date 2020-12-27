import { Component, OnInit, Input } from '@angular/core';
import moment from "moment";
@Component({
  selector: 'app-rating-item',
  templateUrl: './rating-item.component.html',
  styleUrls: ['./rating-item.component.css']
})
export class RatingItemComponent implements OnInit {

  @Input() rating:any = {};

  constructor() { }

  ngOnInit(): void {
  }

  date(){
    return moment(this.rating.timeAdded).format("D/M/yy")
  }

  time(){
    return moment(this.rating.timeAdded).format("HH:mm")
  }
}
