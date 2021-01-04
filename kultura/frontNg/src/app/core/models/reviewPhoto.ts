import {Moment} from 'moment-timezone';

export class ReviewPhoto {
  id?: number;
  width?: number;
  height?: number;
  timeAdded?: Moment;
  reviewId?: number;
  hovering = false;
}
