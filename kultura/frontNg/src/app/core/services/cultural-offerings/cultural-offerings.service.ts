import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CulturalOfferingsService {
  getCulturalOffering(id: Number): any {
    
    //DEV
    return {
      id:1,
      name:"Backa tvrdjava",
      briefInfo:"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut aliquam magna non ipsum imperdiet, id commodo ante hendrerit.",
      overallRating:3.1,
      numReviews:333,
      subcategoryName:"Structures"
    }
  }

  constructor(private httpClient: HttpClient) { }

  delete(id: number): Observable<any> {
    return this.httpClient.delete(`/api/cultural-offerings/${id}`);
  }
}
