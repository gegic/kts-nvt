import { Component, OnInit, Input } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { CulturalOfferingsService } from 'src/app/core/services/cultural-offerings/cultural-offerings.service';

@Component({
  selector: 'app-offering-page',
  templateUrl: './offering-page.component.html',
  styleUrls: ['./offering-page.component.css']
})
export class OfferingPageComponent implements OnInit {

  @Input() id:Number = 1;


  items:MenuItem[]=[
    {label:"Posts", routerLink:"", command:(event)=>this.select(0)},
    {label:"Photos", routerLink:"", command:(event)=>this.select(1)},
    {label:"Reviews", routerLink:"reviews", command:(event)=>this.select(2)},
    {label:"About", routerLink:"", command:(event)=>this.select(3)},
  ];
  active:MenuItem;


  cultural_offering:any;

  culturalOfferingService:CulturalOfferingsService;

  constructor(culturalOfferingService:CulturalOfferingsService) {
    this.culturalOfferingService = culturalOfferingService;
  }

  ngOnInit(): void {
    this.cultural_offering = this.culturalOfferingService.getCulturalOffering(this.id);
    this.active = this.items[0];
  }

  select(i:number){
    this.active = this.items[i];
  }

}
