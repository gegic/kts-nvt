import {animate, query, style, transition, trigger} from '@angular/animations';

export const fader =
  trigger('routeAnimations', [
    transition('* <=> *', [
      style({ opacity: 0 }),
      animate(1000, style({opacity: 1}))
    ])
  ]);
