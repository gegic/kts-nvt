import * as moment from 'moment-timezone';

export class Post {
  id?: number;
  content?: string;
  timeAdded?: moment.Moment;
  culturalOfferingId?: number;
}
