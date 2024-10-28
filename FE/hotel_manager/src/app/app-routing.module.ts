import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './auth/register/register.component';
import { LoginComponent } from './auth/login/login.component';

const routes: Routes = [
  { 
    path: 'admin',
    loadChildren: () => import('./admin/admin.module')
    .then(m => m.AdminModule)
  },
  { 
    path: 'register',
    component: RegisterComponent
  },
  { 
    path: '',
    component: LoginComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
