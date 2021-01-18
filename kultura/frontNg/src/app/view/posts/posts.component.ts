import {
  AfterViewInit,
  Component,
  ElementRef,
  HostListener,
  OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {CulturalOfferingDetailsService} from '../../core/services/cultural-offering-details/cultural-offering-details.service';
import {PostsService} from '../../core/services/posts/posts.service';
import {Post} from '../../core/models/post';
import {BehaviorSubject, Subscription} from 'rxjs';
import {distinctUntilChanged} from 'rxjs/operators';
import {ConfirmationService, MenuItem, MessageService} from 'primeng/api';
import {Router} from '@angular/router';
import {Moment} from 'moment-timezone';
import * as moment from 'moment-timezone';
import {DialogService} from 'primeng/dynamicdialog';
import {TimeUtil} from '../../core/timeUtil';
import {AuthService} from '../../core/services/auth/auth.service';

@Component({
  selector: 'app-posts',
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.scss']
})
export class PostsComponent implements OnInit, AfterViewInit, OnDestroy {

  private subscriptions: Subscription[] = [];

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
  isPostsLoading = false;

  constructor(private detailsService: CulturalOfferingDetailsService,
              private postsService: PostsService,
              private messageService: MessageService,
              private router: Router,
              private confirmationService: ConfirmationService,
              private dialogService: DialogService,
              private authService: AuthService) { }

  ngOnInit(): void {
    this.subscriptions.push(this.detailsService.culturalOffering
      .pipe(distinctUntilChanged()).subscribe(val => {
      if (!!val) {
        this.resetPosts();
      }
    }));
  }

  ngAfterViewInit(): void {
    this.subscriptions.push(this.stickBriefInfo.pipe(distinctUntilChanged()).subscribe(
      val => {
        this.stickBriefInfoValue = val;
      }));
    if (!!this.briefInfoTop) {
      return;
    }
    this.briefInfoTop = this.briefInfo?.nativeElement.offsetTop;
  }

  onScrollDown(): void {
    this.getPosts();
  }

  getPosts(): void {
    if (!this.detailsService.culturalOffering.getValue()) {
      return;
    }
    if (this.page === this.totalPages) {
      this.isPostsLoading = false;
      return;
    }
    this.isPostsLoading = true;
    this.subscriptions.push(
      this.postsService.getPosts(this.detailsService.culturalOffering.getValue()?.id ?? 0, this.page + 1).subscribe(
        val => {
          for (const el of val.content) {
            if (this.postsService.posts.some(post => post.id === el.id)) {
              continue;
            }
            this.postsService.posts.push(el);
          }
          this.page = val.pageable.pageNumber;
          this.totalPages = val.totalPages;
          this.isPostsLoading = false;
        }
      )
    );
  }

  relativeTimeAdded(timeAdded: Moment | undefined): string {
    if (!timeAdded) {
      return 'some time ago.';
    }
    const now = moment();
    const timeAddedMs = moment.utc(timeAdded);
    return TimeUtil.timeDifference(now.valueOf(), timeAddedMs.valueOf());
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
    this.subscriptions.push(
      this.postsService.createPost(this.newPostContent, this.culturalOffering?.id ?? 0)
        .subscribe(val => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Now, everyone will be able to read the post you added.'
        });
        this.resetPosts();
      })
    );
  }

  resetPosts(): void {
    this.isPostsLoading = true;
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
    this.subscriptions.push(
      this.postsService.updatePost(postToUpdate).subscribe(val => {
        this.messageService.add({
          severity: 'success',
          summary: 'Post updated',
          detail: 'The post was updated successfully.'
        });
        this.editContent = '';
        this.isEditDialogOpen = false;
        this.resetPosts();
      })
    );
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
    this.subscriptions.push(
      this.postsService.deletePost(post.id).subscribe(
        () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Post deleted',
            detail: 'The post was deleted successfully.'
          });
          this.resetPosts();
        }
      )
    );
  }

  getUserRole(): string {
    return this.authService.getUserRole();
  }

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  get culturalOffering(): CulturalOffering | undefined {
    return this.detailsService.culturalOffering.getValue();
  }

  get posts(): Post[] {
    return this.postsService.posts;
  }

  ngOnDestroy(): void {
    this.postsService.posts = [];
    this.subscriptions.forEach(s => s.unsubscribe());
  }

}
