export class Place {
  address: string | null;
  ocean: boolean;

  constructor(address: string | null, ocean: boolean) {
    this.address = address;
    this.ocean = ocean;
  }

}
