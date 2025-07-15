import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { SharedModule } from '@shared/shared.module';
import { Button } from 'primeng/button';
import { DataViewModule } from 'primeng/dataview';
import { DepartmentComponent } from './department/department.component';
import { FacultiesRoutingModule } from './faculties-routing.module';
import { FacultiesComponent } from './faculties/faculties.component';
import { FacultyComponent } from './faculty/faculty.component';

@NgModule({
  declarations: [FacultiesComponent, FacultyComponent, DepartmentComponent],
  imports: [
    CommonModule,
    FacultiesRoutingModule,
    SharedModule,
    DataViewModule,
    Button,
  ],
  exports: [FacultiesComponent, FacultyComponent],
})
export class FacultiesModule {}
