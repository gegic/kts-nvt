import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {AdminViewComponent} from './view/admin-view/admin-view.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AppRoutingModule} from './app-routing.module';
import {LoginRegisterComponent} from './view/login-registration/login-register.component';
import {InputTextModule} from 'primeng/inputtext';
import {CardModule} from 'primeng/card';
import {StepsModule} from 'primeng/steps';
import {PasswordModule} from 'primeng/password';
import {ButtonModule} from 'primeng/button';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {EnterEmailComponent} from './view/login-registration/login/enter-email/enter-email.component';
import {EnterPasswordComponent} from './view/login-registration/login/enter-password/enter-password.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ToastModule} from 'primeng/toast';
import {ConfirmationService, MessageService} from 'primeng/api';
import {JwtInterceptor} from './core/interceptors/jwt.interceptor';
import {RegisterEmailComponent} from './view/login-registration/registration/register-email/register-email.component';
import {RegisterNameComponent} from './view/login-registration/registration/register-name/register-name.component';
import {RegisterPasswordComponent} from './view/login-registration/registration/register-password/register-password.component';
import {RegisterSuccessComponent} from './view/login-registration/registration/register-success/register-success.component';
import {RegisterVerifyComponent} from './view/login-registration/registration/register-verify/register-verify.component';
import {UserViewComponent} from './view/user-view/user-view.component';
import {MainViewComponent} from './view/main-view/main-view.component';
import {HomeViewComponent} from './view/home-view/home-view.component';
import {MapViewComponent} from './view/map-view/map-view.component';
import {NavbarComponent} from './view/navbar/navbar.component';
import {AvatarModule} from 'ngx-avatar';
import {MenuModule} from 'primeng/menu';
import {CulturalOfferingAddComponent} from './view/cultural-offering-add/cultural-offering-add.component';
import {AutoCompleteModule} from 'primeng/autocomplete';
import {InputTextareaModule} from 'primeng/inputtextarea';
import {CulturalOfferingPlaceComponent} from './view/cultural-offering-place/cultural-offering-place.component';
import {DialogService, DynamicDialogModule} from 'primeng/dynamicdialog';
import {DropdownModule} from 'primeng/dropdown';
import {ScrollPanelModule} from 'primeng/scrollpanel';
import {NgSelectModule} from '@ng-select/ng-select';
import {ModeratorAddComponent} from './view/moderator-add/moderator-add.component';
import {ModeratorsViewComponent} from './view/moderators-view/moderators-view.component';
import {RippleModule} from 'primeng/ripple';
import {AdminPanelComponent} from './view/admin-panel/admin-panel.component';
import { OfferingSidebarComponent } from './view/offering-sidebar/offering-sidebar.component';
import { OfferingsListComponent } from './view/offerings-list/offerings-list.component';
import { OfferingItemComponent } from './view/offering-item/offering-item.component';
import { MapPopupComponent } from './view/map-popup/map-popup.component';
import { CulturalOfferingDetailsComponent } from './view/cultural-offering-details/cultural-offering-details.component';
import { DetailsNavigationComponent } from './view/details-navigation/details-navigation.component';
import { PostsComponent } from './view/posts/posts.component';
import { PhotosComponent } from './view/photos/photos.component';
import { ReviewsComponent } from './view/reviews/reviews.component';
import { CulturalOfferingAboutComponent } from './view/cultural-offering-about/cultural-offering-about.component';
import {InfiniteScrollModule} from 'ngx-infinite-scroll';
import {VirtualScrollerModule} from 'primeng/virtualscroller';
import {ProgressSpinnerModule} from 'primeng/progressspinner';
import {SkeletonModule} from 'primeng/skeleton';
import {ScrollTop, ScrollTopModule} from 'primeng/scrolltop';
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import {DialogModule} from 'primeng/dialog';
import {FileUpload, FileUploadModule} from 'primeng/fileupload';
import {GalleriaModule} from 'primeng/galleria';
import {ProgressBarModule} from 'primeng/progressbar';
import {RatingModule} from 'primeng/rating';
import { ReviewElementComponent } from './view/review-element/review-element.component';
import { ListViewComponent } from './view/list-view/list-view.component';
import { ListElementComponent } from './view/list-element/list-element.component';
import {InputNumberModule} from 'primeng/inputnumber';
import {SliderModule} from 'primeng/slider';
import { ModeratorListElementComponent } from './view/moderator-list-element/moderator-list-element.component';
import { ModeratorEditComponent } from './view/moderator-edit/moderator-edit.component';
import { CategoryAddComponent } from './view/category-add/category-add.component';
import { CategoriesViewComponent } from './view/categories-view/categories-view.component';
import { CategoryListItemComponent } from './view/category-list-item/category-list-item.component';
import { UserEditComponent } from './view/user-view/user-edit/user-edit.component';
import {TabViewModule} from 'primeng/tabview';

@NgModule({
  declarations: [
    AppComponent,
    AdminViewComponent,
    LoginRegisterComponent,
    EnterEmailComponent,
    EnterPasswordComponent,
    RegisterEmailComponent,
    RegisterNameComponent,
    RegisterPasswordComponent,
    RegisterSuccessComponent,
    RegisterVerifyComponent,
    UserViewComponent,
    MainViewComponent,
    HomeViewComponent,
    MapViewComponent,
    NavbarComponent,
    CulturalOfferingAddComponent,
    CulturalOfferingPlaceComponent,
    ModeratorAddComponent,
    ModeratorsViewComponent,
    AdminPanelComponent,
    OfferingSidebarComponent,
    OfferingsListComponent,
    OfferingItemComponent,
    MapPopupComponent,
    CulturalOfferingDetailsComponent,
    DetailsNavigationComponent,
    PostsComponent,
    PhotosComponent,
    ReviewsComponent,
    CulturalOfferingAboutComponent,
    ReviewElementComponent,
    ListViewComponent,
    ListElementComponent,
    ModeratorListElementComponent,
    ModeratorEditComponent,
    CategoryAddComponent,
    CategoriesViewComponent,
    CategoryListItemComponent,
    UserEditComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    InputTextModule,
    ReactiveFormsModule,
    StepsModule,
    CardModule,
    PasswordModule,
    ButtonModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ToastModule,
    AvatarModule,
    MenuModule,
    AutoCompleteModule,
    InputTextareaModule,
    DynamicDialogModule,
    VirtualScrollerModule,
    ScrollPanelModule,
    DropdownModule,
    NgSelectModule,
    ProgressSpinnerModule,
    RippleModule,
    ScrollPanelModule,
    InfiniteScrollModule,
    SkeletonModule,
    ScrollTopModule,
    ConfirmDialogModule,
    DialogModule,
    FileUploadModule,
    GalleriaModule,
    ProgressBarModule,
    RatingModule,
    InputNumberModule,
    SliderModule,
    AvatarModule,
    TabViewModule
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    MessageService,
    DialogService,
    ConfirmationService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
