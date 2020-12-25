import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-star-component',
  templateUrl: './star-component.component.html',
  styleUrls: ['./star-component.component.css']
})
export class StarComponent implements OnInit {

  @Input() size:number;
  @Input() value:number;
  ratingSize:Number[] = new Array(5);



  constructor() { 
    this.size = 18;
    this.value = 0;
  }

  ngOnInit(): void {
  }
  isMarked(i:Number){
    return i<Math.round(this.value);
  }
}
