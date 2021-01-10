import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  httpClient:HttpClient;
  constructor(httpClient:HttpClient) {
    this.httpClient = httpClient;
  }
  
  getSummary(id: Number) {

    // return this.httpClient.get("/api/reviews/cultural-offering/summary/"+id);

    return {
      ratings:{
        1:140,
        2:120,
        3:200,
        4:230,
        5:150
      }
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
