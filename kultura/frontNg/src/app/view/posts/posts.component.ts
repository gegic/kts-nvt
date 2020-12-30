import {AfterViewChecked, AfterViewInit, Component, ElementRef, HostListener, Input, OnInit, ViewChild} from '@angular/core';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {CulturalOfferingDetailsService} from '../../core/services/cultural-offering-details/cultural-offering-details.service';
import {PostsService} from '../../core/services/posts/posts.service';
import {Post} from '../../core/models/post';
import {BehaviorSubject} from 'rxjs';
import {distinctUntilChanged} from 'rxjs/operators';
import {ConfirmationService, MenuItem, MessageService} from 'primeng/api';
import {Router} from '@angular/router';
import {Moment} from 'moment-timezone';
import * as moment from 'moment-timezone';
import {DialogService} from 'primeng/dynamicdialog';

@Component({
  selector: 'app-posts',
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.scss']
})
export class PostsComponent implements OnInit, AfterViewInit {

  page = -1;
  totalPages = 0;
  stickBriefInfo: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  stickBriefInfoValue = false;
  @ViewChild('briefInfoCard')
  briefInfo!: ElementRef;
  briefInfoTop?: number;
  newPostContent = '';
  isEditDialogOpen = false;
  editContent = '';
  editingPost?: Post;

  constructor(private detailsService: CulturalOfferingDetailsService,
              private postsService: PostsService,
              private messageService: MessageService,
              private router: Router,
              private confirmationService: ConfirmationService,
              private dialogService: DialogService) { }

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
    this.postsService.createPost(this.newPostContent, this.culturalOffering?.id ?? 0)
      .subscribe(val => {
      this.messageService.add({
        severity: 'success',
        summary: 'Success',
        detail: 'Now, everyone will be able to read the post you added.'
      });
    });
    this.resetPosts();
  }

  resetPosts(): void {
    this.postsService.posts = [];
    this.page = -1;
    this.totalPages = 0;
    this.newPostContent = '';
    this.getPosts();
  }

  getMenuItems(post: Post): MenuItem[] {
    return [
      {label: 'Edit', icon: 'pi pi-pencil', command: () => this.editPost(post)},
      {label: 'Delete', icon: 'pi pi-trash', command: () => this.deletePost(post)},
    ];
  }

  editPost(post: Post): void {
    if (!post.content) {
      return;
    }
    this.isEditDialogOpen = true;
    this.editContent = post.content;
    this.editingPost = post;
  }

  onClickEdit(): void {
    if (!this.editingPost) {
      return;
    }
    const postToUpdate = new Post();
    postToUpdate.id = this.editingPost.id;
    postToUpdate.content = this.editContent;
    postToUpdate.timeAdded = this.editingPost.timeAdded;
    postToUpdate.culturalOfferingId = this.editingPost.culturalOfferingId;
    this.postsService.updatePost(postToUpdate).subscribe(val => {
      this.messageService.add({
        severity: 'success',
        summary: 'Post updated',
        detail: 'The post was updated successfully.'
      });
      this.editContent = '';
      this.isEditDialogOpen = false;
      this.resetPosts();
    });
  }

  deletePost(post: Post): void {
    this.confirmationService.confirm(
      {
        message: 'Are you sure that you want to delete this post?',
        acceptLabel: 'Delete',
        rejectLabel: 'Close',
        header: 'Deletion',
        icon: 'pi pi-trash',
        accept: () => this.postDeletionConfirmed(post)
      });
  }

  postDeletionConfirmed(post: Post): void {
    if (!post.id) {
      return;
    }
    this.postsService.deletePost(post.id).subscribe(
      () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Post deleted',
          detail: 'The post was deleted successfully.'
        });
        this.resetPosts();
      }
    );
  }


  get culturalOffering(): CulturalOffering | undefined {
    return this.detailsService.culturalOffering.getValue();
  }

  get posts(): Post[] {
    return this.postsService.posts;
  }
}
