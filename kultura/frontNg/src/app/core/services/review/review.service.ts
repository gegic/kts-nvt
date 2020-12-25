import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  constructor() { }


  getReviews(offering_id:Number){
    return [
      {
        userFirstName:"Miloje Puzigaca",
        timeAdded:new Date(),
        rating:3,
        comment:"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut aliquam magna non ipsum imperdiet, id commodo ante hendrerit. Pellentesque condimentum lorem sed finibus malesuada. Fusce dictum posuere purus ut dictum. Praesent sit amet facilisis lacus."
      },
      {
        userFirstName:"Miloje Puzigaca",
        timeAdded:new Date(),
        rating:3,
        comment:"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut aliquam magna non ipsum imperdiet, id commodo ante hendrerit. Pellentesque condimentum lorem sed finibus malesuada. Fusce dictum posuere purus ut dictum. Praesent sit amet facilisis lacus."
      },
      {
        userFirstName:"Miloje Puzigaca",
        timeAdded:new Date(),
        rating:3,
        comment:"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut aliquam magna non ipsum imperdiet, id commodo ante hendrerit. Pellentesque condimentum lorem sed finibus malesuada. Fusce dictum posuere purus ut dictum. Praesent sit amet facilisis lacus."
      },
    ]
  }
}
