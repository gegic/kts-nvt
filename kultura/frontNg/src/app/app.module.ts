import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {FileUploadModule} from 'primeng/fileupload';
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
import {MessageService} from 'primeng/api';
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
import {VirtualScrollerModule} from 'primeng/virtualscroller';
import {ScrollPanelModule} from 'primeng/scrollpanel';
import {NgSelectModule} from '@ng-select/ng-select';
import {ProgressSpinnerModule} from 'primeng/progressspinner';
import {ModeratorAddComponent} from './view/moderator-add/moderator-add.component';
import {ModeratorsViewComponent} from './view/moderators-view/moderators-view.component';
import {RippleModule} from 'primeng/ripple';
import {AdminPanelComponent} from './view/admin-panel/admin-panel.component';
import { OfferingSidebarComponent } from './view/offering-sidebar/offering-sidebar.component';
import { OfferingsListComponent } from './view/offerings-list/offerings-list.component';
import { OfferingItemComponent } from './view/offering-item/offering-item.component';
import { MapPopupComponent } from './view/map-popup/map-popup.component';
import { OfferingReviewComponent } from './view/offering-page/offering-review/offering-review.component';
import { OfferingAllRatingsComponent } from './view/offering-all-ratings/offering-all-ratings.component';
import { OfferingRatingsListComponent } from './view/offering-page/offering-review/offering-ratings-list/offering-ratings-list.component';
import { RatingItemComponent } from './view/offering-page/offering-review/offering-ratings-list/rating-item/rating-item.component';
import {ChartModule} from 'primeng/chart';
import { StarComponent } from './components/star-component/star-component.component';
import { OfferingPageComponent } from './view/offering-page/offering-page.component';
import {TabMenuModule} from 'primeng/tabmenu';
import {MenuItem} from 'primeng/api';
import { OfferingPhotosComponent } from './view/offering-page/offering-photos/offering-photos.component';

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
    OfferingReviewComponent,
    OfferingAllRatingsComponent,
    OfferingRatingsListComponent,
    RatingItemComponent,
    StarComponent,
    OfferingPageComponent,
    OfferingPhotosComponent,
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
    FileUploadModule,
    ChartModule,
    TabMenuModule,
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    MessageService,
    DialogService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
