import {Moment} from 'moment-timezone';

export class Review {
  id?: number;
  rating = 0;
  comment = '';
  timeAdded?: Moment;
  culturalOfferingId?: number;
  userId?: number;
  userFirstName?: string;
  userLastName?: string;
  userEmail?: string;
  photos?: number[];
}
