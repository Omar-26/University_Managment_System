import { Component, OnInit } from '@angular/core';
import { FacultyDTO, FacultyWithDepartments } from '@core/models/faculty.model';
import { FacultyService } from '@core/services/faculty.service';
import { fetchAndConcat } from '@core/utils/rxjs/fetch.and.concat';
import { map } from 'rxjs';

@Component({
  selector: 'app-faculties',
  templateUrl: './faculties.component.html',
  styleUrl: './faculties.component.scss',
})

/**
 * Component for displaying a list of faculties.
 * This component fetches all faculties and their associated departments,
 * allowing users to view department counts within each faculty.
 * @export
 * @class FacultiesComponent
 */
export class FacultiesComponent implements OnInit {
  faculties: FacultyWithDepartments[] = [];

  constructor(private facultyService: FacultyService) {}

  ngOnInit() {
    this.loadFaculties();
  }

  /**
   * Loads all faculties and their associated departments.
   * It fetches faculty details and maps them to include department counts.
   */
  loadFaculties() {
    fetchAndConcat(this.facultyService.getFaculties(), (faculty: FacultyDTO) =>
      this.facultyService
        .getDepartmentCount(faculty.id!)
        .pipe(map((count) => ({ departmentCount: count })))
    ).subscribe((faculties: FacultyWithDepartments[]) => {
      this.faculties = faculties;
    });
  }
}
