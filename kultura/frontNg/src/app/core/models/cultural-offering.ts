import {Moment} from 'moment-timezone';

export class CulturalOffering {
  id?: number;
  name?: string;
  briefInfo?: string;
  latitude?: number;
  longitude?: number;
  address?: string;
  photoId?: number;
  overallRating?: number;
  numReviews?: number;
  lastChange?: Moment;
  additionalInfo?: string;
  subcategoryId?: number;
  subcategoryName?: string;
  numSubscribed?: number;
  numPhotos?: number;
  categoryName?: string;
  categoryId?: number;
  subscribed?: boolean;
}
