import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {PhotoService} from '../../core/services/photos/photo.service';
import {CulturalOfferingDetailsService} from '../../core/services/cultural-offering-details/cultural-offering-details.service';
import {CulturalOfferingPhoto} from '../../core/models/culturalOfferingPhoto';
import {Confirmation, ConfirmationService, MessageService} from 'primeng/api';
import {Observable} from 'rxjs';
import {AuthService} from '../../core/services/auth/auth.service';

@Component({
  selector: 'app-photos',
  templateUrl: './photos.component.html',
  styleUrls: ['./photos.component.scss']
})
export class PhotosComponent implements OnInit, OnDestroy {

  page = -1;
  totalPages = 0;
  isOpenAddDialog = false;
  galleriaVisible = false;
  activeIndex = -1;
  uploadLoading = false;

  @ViewChild('upload')
  upload!: any;

  constructor(private detailsService: CulturalOfferingDetailsService,
              private photoService: PhotoService,
              private messageService: MessageService,
              private confirmationService: ConfirmationService,
              private authService: AuthService) { }

  ngOnInit(): void {
    this.detailsService.culturalOffering.subscribe(val => {
      if (!!val) {
        this.photoService.photos = [];
        this.getPhotos();
      }
    });
  }

  getPhotos(): Observable<any> | null {
    if (!this.detailsService.culturalOffering.getValue()) {
      return null;
    }
    if (this.page === this.totalPages) {
      return null;
    }
    const obs = this.photoService.getPhotos(this.detailsService.culturalOffering.getValue()?.id ?? 0, this.page + 1);
    obs.subscribe(
      val => {
        for (const el of val.content) {
          if (this.photoService.photos.some(p => p.id === el.id)) {
            continue;
          }
          this.photoService.photos.push(el);
        }
        this.page = val.pageable.pageNumber;
        this.totalPages = val.totalPages;
      }
    );
    return obs;
  }

  onScrollDown(): void {
    this.getPhotos();
  }

  getThumbnailUrl(photo: CulturalOfferingPhoto): string {
    return `/photos/thumbnail/${photo.id ?? -1}.png`;
  }

  getPhotoUrl(photo: CulturalOfferingPhoto): string {
    return `/photos/${photo.id ?? -1}.png`;
  }

  clearFiles(): void {
    this.upload.clear();
  }

  onClickUpload(event: any): void {
    const file: File = event.files[0];
    this.uploadLoading = true;
    this.photoService.addPhoto(file, this.detailsService.culturalOffering.getValue()?.id ?? 0).subscribe(
      () => {
        this.isOpenAddDialog = false;
        this.messageService.add({
          severity: 'success',
          summary: 'Added',
          detail: 'A photo was added successfully'
        });
        this.uploadLoading = false;
        this.resetPhotos();
        this.clearFiles();
      }
    );
  }

  resetPhotos(): void {
    this.photoService.photos = [];
    this.page = -1;
    this.totalPages = 0;
    this.getPhotos();
  }

  getUserRole(): string {
    return this.authService.getUserRole();
  }


  onPhotoHoverStart(photo: CulturalOfferingPhoto): void {
    photo.hovering = true;
  }

  onPhotoHoverEnd(photo: CulturalOfferingPhoto): void {
    photo.hovering = false;
  }

  onClickDelete(): void {
    const photo = this.photos[this.activeIndex];
    this.confirmationService.confirm(
      {
        message: 'Are you sure that you want to delete this photo',
        acceptLabel: 'Delete',
        rejectLabel: 'Close',
        header: 'Deletion',
        icon: 'pi pi-trash',
        accept: () => this.photoDeletionConfirmed(photo)
      });
  }

  photoDeletionConfirmed(photo: CulturalOfferingPhoto): void {
    if (!photo.id) {
      return;
    }
    this.photoService.delete(photo.id).subscribe(() => {
      this.messageService.add({
        severity: 'success',
        summary: 'Photo deleted',
        detail: 'The photo was deleted successfully.'
      });
      this.galleriaVisible = false;
      this.resetPhotos();
    });
  }

  imageClick(index: number): void {
    if ((index >= this.photos.length - 4) && (this.page < this.totalPages)) {
      this.getPhotos()?.subscribe(() => {
          this.activeIndex = index;
          this.galleriaVisible = true;
        });
    } else {
      this.activeIndex = index;
      this.galleriaVisible = true;
    }
  }

  get photos(): CulturalOfferingPhoto[] {
    return this.photoService.photos;
  }

  ngOnDestroy(): void {
    this.photoService.photos = [];
  }

}
