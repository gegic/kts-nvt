import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  
  constructor() { }
  
  getSummary(id: Number) {
    return {
      ratings:{
        1:140,
        2:120,
        3:200,
        4:230,
        5:150
      },
      ratingsSize:333,
      rating:3.1
    };
  }

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
