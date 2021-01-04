import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ReviewGalleriaService {

  visible = false;
  value?: any[];
  activeIndex = 0;
  constructor() { }
}
