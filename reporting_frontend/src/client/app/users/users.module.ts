import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {CommonModule} from '@angular/common';
import {UsersComponent} from './users.component';


@NgModule({
  imports: [CommonModule, RouterModule],
  declarations: [UsersComponent],
  exports: [UsersComponent],
  providers: []
})

export class UsersModule {
}
