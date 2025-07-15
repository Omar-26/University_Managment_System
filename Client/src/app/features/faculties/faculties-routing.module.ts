import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DepartmentComponent } from './department/department.component';
import { FacultiesComponent } from './faculties/faculties.component';
import { FacultyComponent } from './faculty/faculty.component';

const routes: Routes = [
  {
    path: '',
    component: FacultiesComponent,
    data: { title: 'Faculties', breadcrumb: 'Faculties' },
  },
  {
    path: ':id',
    component: FacultyComponent,
    data: { title: 'Faculty Details', breadcrumb: 'Details' },
  },
  {
    path: 'departments/:id',
    component: DepartmentComponent,
    data: { title: 'Department Details', breadcrumb: 'Department Details' },
  },
  //   {
  //     path: 'course/:code',
  //     component: DepartmentComponent,
  //     data: { title: 'Department Details', breadcrumb: 'Department Details' },
  //   },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class FacultiesRoutingModule {}
