import {Moment} from 'moment-timezone';

export class CulturalOfferingPhoto {
  id?: number;
  width?: number;
  height?: number;
  timeAdded?: Moment;
  culturalOfferingId?: number;
  hovering = false;
}
