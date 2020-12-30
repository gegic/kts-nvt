import {AfterViewChecked, AfterViewInit, Component, ElementRef, HostListener, Input, OnInit, ViewChild} from '@angular/core';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {CulturalOfferingDetailsService} from '../../core/services/cultural-offering-details/cultural-offering-details.service';
import {PostsService} from '../../core/services/posts/posts.service';
import {Post} from '../../core/models/post';
import {BehaviorSubject} from 'rxjs';
import {distinctUntilChanged} from 'rxjs/operators';
import {MenuItem, MessageService} from 'primeng/api';
import {Router} from '@angular/router';
import {Moment} from 'moment-timezone';
import * as moment from 'moment-timezone';

@Component({
  selector: 'app-posts',
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.scss']
})
export class PostsComponent implements OnInit, AfterViewInit {

  menuItems: MenuItem[] = [
    {label: 'Edit', icon: 'pi pi-pencil', command: e => console.log('EDIT')},
    {label: 'Delete', icon: 'pi pi-trash', command: e => console.log('DELETE')},

  ];

  page = -1;
  totalPages = 0;
  stickBriefInfo: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  stickBriefInfoValue = false;
  @ViewChild('briefInfoCard')
  briefInfo!: ElementRef;
  briefInfoTop?: number;
  newPostContent = '';

  constructor(private detailsService: CulturalOfferingDetailsService,
              private postsService: PostsService,
              private messageService: MessageService,
              private router: Router) { }

  ngOnInit(): void {
    this.detailsService.culturalOffering.subscribe(val => {
      if (!!val) {
        this.getPosts();
      }
    });

    this.stickBriefInfo.pipe(distinctUntilChanged()).subscribe(
      val => {
        this.stickBriefInfoValue = val;
      });
  }

  ngAfterViewInit(): void {
    if (!!this.briefInfoTop) {
      return;
    }
    this.briefInfoTop = this.briefInfo?.nativeElement.offsetTop;
    console.log(this.briefInfoTop);
  }

  onScrollDown(): void {
    this.getPosts();
  }

  getPosts(): void {
    if (!this.detailsService.culturalOffering.getValue()) {
      return;
    }
    if (this.page === this.totalPages) {
      return;
    }
    this.postsService.getPosts(this.detailsService.culturalOffering.getValue()?.id ?? 0, this.page + 1).subscribe(
      val => {
        this.postsService.posts = this.postsService.posts.concat(val.content);
        this.page = val.pageable.pageNumber;
        this.totalPages = val.totalPages;
      }
    );
  }

  private timeDifference(current: number, previous: number): string {

    const msPerMinute = 60 * 1000;
    const msPerHour = msPerMinute * 60;
    const msPerDay = msPerHour * 24;
    const msPerMonth = msPerDay * 30;
    const msPerYear = msPerDay * 365;

    const elapsed = current - previous;

    if (elapsed < msPerMinute) {
      return 'few seconds ago';
    }

    else if (elapsed < msPerHour) {
      return Math.round(elapsed / msPerMinute) + ' minutes ago';
    }

    else if (elapsed < msPerDay ) {
      return Math.round(elapsed / msPerHour ) + ' hours ago';
    }

    else if (elapsed < msPerMonth) {
      return 'approximately ' + Math.round(elapsed / msPerDay) + ' days ago';
    }

    else if (elapsed < msPerYear) {
      return 'approximately ' + Math.round(elapsed / msPerMonth) + ' months ago';
    }

    else {
      return 'approximately ' + Math.round(elapsed / msPerYear ) + ' years ago';
    }
  }

  relativeTimeAdded(timeAdded: Moment | undefined): string {
    if (!timeAdded) {
      return 'some time ago.';
    }
    const now = moment();
    const timeAddedMs = moment.utc(timeAdded);
    return this.timeDifference(now.valueOf(), timeAddedMs.valueOf());
  }

  @HostListener('window:scroll', ['$event'])
  onScroll(event: any): void {
    if (!this.briefInfoTop) {
      return;
    }

    if (this.briefInfo.nativeElement.className === 'relative-info') {
      this.briefInfoTop = this.briefInfo.nativeElement.offsetTop;
    }

    const screenTop = document.documentElement.scrollTop;
    const difference = (this.briefInfoTop ?? 0) - screenTop;

    this.stickBriefInfo.next(difference <= 75);
  }

  onClickAnnounce(): void {
    if (!this.newPostContent) {
      this.messageService.add({
        severity: 'error',
        summary: 'Empty announcement',
        detail: 'Announcement cannot be empty.'
      });
    }
    if (!this.culturalOffering) {
      return;
    }
    this.postsService.createPost(this.newPostContent, this.culturalOffering?.id ?? 0).subscribe(val => {
      this.postsService.posts = [];
      this.page = -1;
      this.totalPages = 0;
      this.newPostContent = '';
      this.getPosts();
    });
  }

  get culturalOffering(): CulturalOffering | undefined {
    return this.detailsService.culturalOffering.getValue();
  }

  get posts(): Post[] {
    return this.postsService.posts;
  }
}
