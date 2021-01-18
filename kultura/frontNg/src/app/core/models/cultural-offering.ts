import {Moment} from 'moment-timezone';

export class CulturalOffering {
  id = -1;
  name = '';
  briefInfo = '';
  latitude = 0;
  longitude = 0;
  address = '';
  photoId?: number;
  overallRating = 0;
  numReviews = 0;
  additionalInfo?: string;
  subcategoryId?: number;
  subcategoryName = '';
  numSubscribed = 0;
  numPhotos?: number;
  categoryName = '';
  categoryId?: number;
  subscribed?: boolean;
}
