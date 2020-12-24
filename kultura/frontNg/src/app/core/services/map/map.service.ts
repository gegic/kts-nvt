import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';

export const ZOOM_IMPORTANT = 10;
export const ZOOM_REGULAR = 15;

@Injectable({
  providedIn: 'root'
})
export class MapService {

  zoom = new BehaviorSubject<number>(0);

  constructor() {}
}
