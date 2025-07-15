import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DepartmentDTO } from '@core/models/department.model';
import { FacultyDTO } from '@core/models/faculty.model';
import { DepartmentService } from '@core/services/department.service';
import { fetchAndConcat } from '@core/utils/rxjs/fetch.and.concat'; // Add this import
import { map } from 'rxjs/operators'; // Add this import

// Add this interface
interface DepartmentWithCourses extends DepartmentDTO {
  numOfCourses: number;
}

@Component({
  selector: 'app-faculty',
  templateUrl: './faculty.component.html',
  styleUrl: './faculty.component.scss',
})
export class FacultyComponent implements OnInit {
  facultyId!: number;
  faculty!: FacultyDTO;
  departments: DepartmentWithCourses[] = []; // Update type

  constructor(
    private route: ActivatedRoute,
    private departmentService: DepartmentService
  ) {}

  ngOnInit() {
    this.facultyId = +this.route.snapshot.paramMap.get('id')!;
    this.loadDepartments();
  }

  loadDepartments() {
    //! TODO - Implement getDepartmentsByFaculty in backend and use it here instead of filtering client-side.
    fetchAndConcat(
      this.departmentService
        .getDepartments()
        .pipe(
          map((departments) =>
            departments.filter(
              (department) => department.facultyId === this.facultyId
            )
          )
        ),
      (department: DepartmentDTO) =>
        this.departmentService.getCoursesCount(department.id!)
    ).subscribe({
      next: (departments: DepartmentWithCourses[]) => {
        this.departments = departments;
        console.log('Departments loaded:', departments);
      },
      error: (error) => {
        console.error('Error loading departments with course counts:', error);
        this.departmentService.getDepartments().subscribe((departments) => {
          this.departments = departments
            .filter((department) => department.facultyId === this.facultyId)
            .map((dept) => ({ ...dept, numOfCourses: 0 })); // Add default course count
        });
      },
    });
  }
}
