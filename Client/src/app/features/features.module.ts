import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from '@shared/shared.module';
import { Button } from 'primeng/button';
import { FacultiesModule } from './faculties/faculties.module';
import { HomeComponent } from './home/home.component';

@NgModule({
  declarations: [HomeComponent],
  imports: [CommonModule, RouterModule, SharedModule, FacultiesModule, Button],
  exports: [HomeComponent, FacultiesModule],
})
export class FeaturesModule {}
