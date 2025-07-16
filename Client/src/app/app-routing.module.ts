import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FacultiesComponent } from '@features/faculties/faculties/faculties.component';
import { HomeComponent } from '@features/home/home.component';
import { InstructorComponent } from '@features/instructor/instructor.component';
import { StudentComponent } from '@features/student/student.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  {
    path: 'faculties',
    loadChildren: () =>
      import('./features/faculties/faculties.module').then(
        (m) => m.FacultiesModule
      ),
  },
  { path: 'student/:id', component: StudentComponent },
  { path: 'instructor/:id', component: InstructorComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
