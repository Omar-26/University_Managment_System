import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CourseDTO, CourseWithDetails } from '@core/models/course.model';
import { CourseService } from '@core/services/course.service';
import { DepartmentService } from '@core/services/department.service';
import { LevelService } from '@core/services/level.service';
import { fetchAndConcat } from '@core/utils/rxjs/fetch.and.concat';
import { map } from 'rxjs';

@Component({
  selector: 'app-department',
  templateUrl: './department.component.html',
  styleUrl: './department.component.scss',
})

/**
 * Component for displaying department details and courses.
 * This component fetches the department information and its associated courses,
 * allowing users to view course details and select a course for further actions.
 * @export
 * @class DepartmentComponent
 */
export class DepartmentComponent {
  departmentId!: number;
  departmentName!: string;
  //! TODO Add Pagination
  courses!: CourseWithDetails[];
  selectedCourse!: CourseWithDetails;

  constructor(
    private route: ActivatedRoute,
    private departmentService: DepartmentService,
    private levelService: LevelService
  ) {}

  ngOnInit() {
    this.departmentId = +this.route.snapshot.paramMap.get('id')!;
    this.getDepartment();
    this.loadCourses();
  }

  /**
   * Fetches the department details based on the ID from the route parameters.
   * It retrieves the department name to display in the component.
   */
  getDepartment() {
    this.departmentService
      .getDepartment(this.departmentId)
      .subscribe((department) => {
        this.departmentName = department.name;
      });
  }

  /**
   * Loads the courses associated with the department.
   * It fetches course details and maps them to include level names.
   */
  loadCourses() {
    fetchAndConcat(
      this.departmentService.getCourses(this.departmentId),
      (course: CourseDTO) =>
        this.levelService
          .getLevel(course.levelId!)
          .pipe(map((level) => ({ levelName: level.name })))
    ).subscribe((courses: CourseWithDetails[]) => {
      this.courses = courses;
    });
  }

  /**
   * Handles the selection of a course.
   * This method is called when a user clicks on a course to view its details.
   * @param course The selected course to display details for.
   */
  //! TODO after adding pagination just pass the course itself
  onCourseSelect(course: CourseDTO) {
    this.selectedCourse = course;
  }
}
