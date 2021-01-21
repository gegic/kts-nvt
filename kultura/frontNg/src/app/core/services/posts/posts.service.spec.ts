import {fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';

import { PostsService } from './posts.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient} from '@angular/common/http';
import {Post} from '../../models/post';

describe('PostsService', () => {
  let injector;
  let service: PostsService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PostsService]
    });

    injector = getTestBed();
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
    service = TestBed.inject(PostsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('getPosts() should query url and get all posts', fakeAsync(() => {
    let posts: Post[] = [];
    const mockPosts: Post[] = [
      {
        id: 1,
        content: 'Mnogooo dobro mestooo xD',
        culturalOfferingId: 1,
      },
      {
        id: 2,
        content: 'Dobro mestooo xD',
        culturalOfferingId: 1,
      },
      {
        id: 3,
        content: 'Odlicno mestooo xD',
        culturalOfferingId: 1,
      }];

    service.getPosts(1, 1).subscribe(data => {
      posts = data;
    });

    const req = httpMock.expectOne('/api/posts/cultural-offering/1?page=1');
    expect(req.request.method).toBe('GET');
    req.flush(mockPosts);

    tick();

    expect(posts.length).toEqual(3, 'should contain given amount of moderators');

    expect(posts[0].id).toEqual(1);
    expect(posts[0].content).toEqual('Mnogooo dobro mestooo xD');
    expect(posts[0].culturalOfferingId).toEqual(1);

    expect(posts[1].id).toEqual(2);
    expect(posts[1].content).toEqual('Dobro mestooo xD');
    expect(posts[1].culturalOfferingId).toEqual(1);

    expect(posts[2].id).toEqual(3);
    expect(posts[2].content).toEqual('Odlicno mestooo xD');
    expect(posts[2].culturalOfferingId).toEqual(1);
  }));

  it('createPost()  should query url and save a post', fakeAsync(() => {
    let newPost: Post = {};

    const mockPost: Post =
      {
        id: 1,
        content: 'Mnogooo dobro mestooo xD',
        culturalOfferingId: 1,
      };

    service.createPost('Mnogooo dobro mestooo xD', 1).subscribe(data => newPost = data);


    const req = httpMock.expectOne('/api/posts');
    expect(req.request.method).toBe('POST');
    req.flush(mockPost);

    tick();

    expect(newPost).toBeDefined();
    expect(newPost.id).toEqual(1);
    expect(newPost.content).toEqual('Mnogooo dobro mestooo xD');
    expect(newPost.culturalOfferingId).toEqual(1);
  }));

  it('update() should query url and edit a post', fakeAsync(() => {
    let updatePost: Post = {
      content: 'Mnogooo dobro mestooo xD',
      culturalOfferingId: 1,
    };


    const mockPost: Post =
      {
        id: 1,
        content: 'Dobro mestooo xD',
        culturalOfferingId: 1,
      };

    service.updatePost(updatePost).subscribe(res => updatePost = res
    );

    const req = httpMock.expectOne('/api/posts');
    expect(req.request.method).toBe('PUT');
    req.flush(mockPost);

    tick();
    expect(updatePost).toBeDefined();
    expect(updatePost.id).toEqual(1);
    expect(updatePost.content).toEqual('Dobro mestooo xD');
    expect(updatePost.culturalOfferingId).toEqual(1);
  }));

  it('delete() should query url and delete a moderator', () => {
    service.deletePost(1).subscribe(res => { });

    const req = httpMock.expectOne(`/api/posts/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});
