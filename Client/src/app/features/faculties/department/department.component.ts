import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CourseDTO } from '@core/models/course.model';
import { CourseService } from '@core/services/course.service';
import { DepartmentService } from '@core/services/department.service';

@Component({
  selector: 'app-department',
  templateUrl: './department.component.html',
  styleUrl: './department.component.scss',
})
export class DepartmentComponent {
  departmentId!: number;
  courses!: CourseDTO[];
  selectedCourse!: CourseDTO;

  constructor(
    private route: ActivatedRoute,
    private courseService: CourseService
  ) {}

  ngOnInit() {
    this.departmentId = +this.route.snapshot.paramMap.get('id')!;
    this.courseService.getCourses().subscribe((courses: CourseDTO[]) => {
      this.courses = courses.filter(
        (course: CourseDTO) => course.departmentId === this.departmentId
      );
    });
  }

    onCourseSelect(course: CourseDTO) {
        this.selectedCourse = course;
    }
}
