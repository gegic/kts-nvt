import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';

@Component({
  selector: 'app-details-navigation',
  templateUrl: './details-navigation.component.html',
  styleUrls: ['./details-navigation.component.scss']
})
export class DetailsNavigationComponent {
  @Input()
  navigationItems: {label: string, link: string, icon?: string}[] = [];
}
