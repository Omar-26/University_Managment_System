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
export class FacultiesComponent implements OnInit {
  faculties: FacultyWithDepartments[] = [];

  constructor(private facultyService: FacultyService) {}

  ngOnInit() {
    this.loadFaculties();
  }

  loadFaculties() {
    fetchAndConcat(this.facultyService.getFaculties(), (faculty: FacultyDTO) =>
      this.facultyService
        .getNumOfDepartments(faculty.id!)
        .pipe(map((count) => ({ numOfDepartments: count })))
    ).subscribe((faculties: FacultyWithDepartments[]) => {
      this.faculties = faculties;
    });
  }
}
