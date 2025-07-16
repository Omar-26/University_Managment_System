import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {
  DepartmentDTO,
  DepartmentWithCourses,
} from '@core/models/department.model';
import { FacultyDTO } from '@core/models/faculty.model';
import { DepartmentService } from '@core/services/department.service';
import { FacultyService } from '@core/services/faculty.service';
import { fetchAndConcat } from '@core/utils/rxjs/fetch.and.concat'; // Add this import
import { map } from 'rxjs/operators'; // Add this import

@Component({
  selector: 'app-faculty',
  templateUrl: './faculty.component.html',
  styleUrl: './faculty.component.scss',
})

/**
 * Component for displaying faculty details and associated departments.
 * This component fetches the faculty by ID and loads its departments,
 * including the count of courses in each department.
 * @export
 * @class FacultyComponent
 */
export class FacultyComponent implements OnInit {
  facultyId!: number;
  faculty!: FacultyDTO;
  departments!: DepartmentWithCourses[];

  constructor(
    private route: ActivatedRoute,
    private facultyService: FacultyService,
    private departmentService: DepartmentService
  ) {}

  ngOnInit() {
    this.facultyId = +this.route.snapshot.paramMap.get('id')!;
    this.loadDepartments();
  }

  /**
   * Loads the departments associated with the faculty.
   * It fetches department details and maps them to include course counts.
   */
  loadDepartments() {
    fetchAndConcat(
      this.facultyService.getDepartments(this.facultyId),
      (department: DepartmentDTO) =>
        this.departmentService
          .getCoursesCount(department.id!)
          .pipe(map((count) => ({ courseCount: count })))
    ).subscribe((departments: DepartmentWithCourses[]) => {
      this.departments = departments;
    });
  }
}
