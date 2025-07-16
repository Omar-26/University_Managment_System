import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from '@shared/shared.module';
import { AnimateOnScrollModule } from 'primeng/animateonscroll';
import { Button } from 'primeng/button';
import { DataViewModule } from 'primeng/dataview';
import { DropdownModule } from 'primeng/dropdown';
import { MessageModule } from 'primeng/message';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { FacultiesModule } from './faculties/faculties.module';
import { HomeComponent } from './home/home.component';
import { InstructorComponent } from './instructor/instructor.component';
import { StudentComponent } from './student/student.component';

@NgModule({
  declarations: [HomeComponent, StudentComponent, InstructorComponent],
  imports: [
    CommonModule,
    RouterModule,
    SharedModule,
    FacultiesModule,
    Button,
    AnimateOnScrollModule,
    DataViewModule,
    ProgressSpinnerModule,
    MessageModule,
    DropdownModule,
  ],
  exports: [HomeComponent, FacultiesModule],
})
export class FeaturesModule {}
