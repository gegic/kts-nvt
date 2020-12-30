import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Post} from '../../models/post';
import * as moment from 'moment-timezone';

@Injectable({
  providedIn: 'root'
})
export class PostsService {

  posts: Post[] = [];

  constructor(private httpClient: HttpClient) { }

  getPosts(culturalOfferingId: number, page: number): Observable<any> {
    return this.httpClient.get(`/api/posts/cultural-offering/${culturalOfferingId}?page=${page}`);
  }

  createPost(content: string, culturalOfferingId: number): Observable<any> {
    const post = new Post();
    post.content = content;
    post.culturalOfferingId = culturalOfferingId;
    post.timeAdded = moment();
    return this.httpClient.post('/api/posts', post);
  }
}
