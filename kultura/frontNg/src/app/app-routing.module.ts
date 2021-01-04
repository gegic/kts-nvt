import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginRegisterComponent} from './view/login-registration/login-register.component';
import {EnterEmailComponent} from './view/login-registration/login/enter-email/enter-email.component';
import {EnterPasswordComponent} from './view/login-registration/login/enter-password/enter-password.component';
import {AuthGuard} from './core/guards/auth/auth.guard';
import {RegisterEmailComponent} from './view/login-registration/registration/register-email/register-email.component';
import {RegisterNameComponent} from './view/login-registration/registration/register-name/register-name.component';
import {RegisterPasswordComponent} from './view/login-registration/registration/register-password/register-password.component';
import {RegisterSuccessComponent} from './view/login-registration/registration/register-success/register-success.component';
import {RegisterVerifyComponent} from './view/login-registration/registration/register-verify/register-verify.component';
import {MainViewComponent} from './view/main-view/main-view.component';
import {MapViewComponent} from './view/map-view/map-view.component';
import {CulturalOfferingAddComponent} from './view/cultural-offering-add/cultural-offering-add.component';
import {ModeratorAddComponent} from './view/moderator-add/moderator-add.component';
import {AdminViewComponent} from './view/admin-view/admin-view.component';
import {ModeratorsViewComponent} from './view/moderators-view/moderators-view.component';
import {HomeViewComponent} from './view/home-view/home-view.component';
import {AdminPanelComponent} from './view/admin-panel/admin-panel.component';
import {CulturalOfferingDetailsComponent} from './view/cultural-offering-details/cultural-offering-details.component';
import {PostsComponent} from './view/posts/posts.component';
import {PhotosComponent} from './view/photos/photos.component';
import {ReviewsComponent} from './view/reviews/reviews.component';
import {CulturalOfferingAboutComponent} from './view/cultural-offering-about/cultural-offering-about.component';
import {ListViewComponent} from './view/list-view/list-view.component';

const routes: Routes = [
  {
    path: '', component: MainViewComponent, children: [
      { path: '', component: MapViewComponent, data: {roles: ['MODERATOR', 'USER']}, canActivate: [AuthGuard] },
      { path: 'list-view', component: ListViewComponent, data: {roles: ['MODERATOR', 'USER']}, canActivate: [AuthGuard] },
      { path: 'cultural-offering/:id', redirectTo: 'cultural-offering/:id/posts'},
      { path: 'cultural-offering/:id', component: CulturalOfferingDetailsComponent, data: {roles: ['MODERATOR', 'USER']},
        canActivate: [AuthGuard],
        children: [
          { path: 'posts', component: PostsComponent },
          { path: 'photos', component: PhotosComponent },
          { path: 'reviews', component: ReviewsComponent },
          { path: 'about', component: CulturalOfferingAboutComponent }
        ]
      },
      { path: 'create-offering', component: CulturalOfferingAddComponent, data: {roles: ['MODERATOR'], mode: 'add'}, canActivate: [AuthGuard] },
      { path: 'edit-offering/:id', component: CulturalOfferingAddComponent, data: {roles: ['MODERATOR'], mode: 'edit'}, canActivate: [AuthGuard] },
      { path: 'admin-panel', component: ModeratorsViewComponent, data: {roles: ['ADMIN']}, canActivate: [AuthGuard] },
      { path: 'add-moderator', component: ModeratorAddComponent, data: {roles: ['ADMIN']}, canActivate: [AuthGuard] }
    ]
  },
  {
    path: 'login', component: LoginRegisterComponent, canActivate: [AuthGuard],
    children: [
      {path: '', component: EnterEmailComponent, data: {animation: 'EnterEmail'}},
      {path: 'password', component: EnterPasswordComponent, data: {animation: 'EnterPassword'}}
    ]
  },
  {
    path: 'register', component: LoginRegisterComponent, canActivate: [AuthGuard],
    children: [
      {path: '', component: RegisterEmailComponent, data: {animation: 'RegisterEmail'}},
      {path: 'name', component: RegisterNameComponent, data: {animation: 'RegisterName'}},
      {path: 'password', component: RegisterPasswordComponent, data: {animation: 'RegisterPassword'}},
      {path: 'success', component: RegisterSuccessComponent, data: {animation: 'RegisterSuccess'}}
    ]
  },
  {
    path: 'verify', component: LoginRegisterComponent, canActivate: [AuthGuard],
    children: [
      {path: ':id', component: RegisterVerifyComponent}
    ]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
