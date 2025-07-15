import { Injectable } from '@angular/core';
import { ApiPaths } from '@core/constants/api-paths';
import { FacultyDTO } from '@core/models/faculty.model';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root',
})
export class FacultyService {
  constructor(private apiService: ApiService) {}

  getFaculties(): Observable<FacultyDTO[]> {
    return this.apiService.get<FacultyDTO[]>(ApiPaths.FACULTIES).pipe(
      map((response) => response || []),
      catchError((error) => {
        console.error('FacultyService: Error fetching faculties', error);
        return throwError(() => new Error('Failed to load faculties'));
      })
    );
  }

  getFaculty(id: number): Observable<FacultyDTO> {
    return this.apiService.get<FacultyDTO>(`${ApiPaths.FACULTIES}/${id}`).pipe(
      catchError((error) => {
        console.error('FacultyService: Error fetching faculty', error);
        return throwError(() => new Error('Failed to load faculty'));
      })
    );
  }

  createFaculty(faculty: Partial<FacultyDTO>): Observable<FacultyDTO> {
    return this.apiService.post<FacultyDTO>(ApiPaths.FACULTIES, faculty).pipe(
      catchError((error) => {
        console.error('FacultyService: Error creating faculty', error);
        return throwError(() => new Error('Failed to create faculty'));
      })
    );
  }

  updateFaculty(
    id: number,
    faculty: Partial<FacultyDTO>
  ): Observable<FacultyDTO> {
    return this.apiService
      .put<FacultyDTO>(`${ApiPaths.FACULTIES}/${id}`, faculty)
      .pipe(
        catchError((error) => {
          console.error('FacultyService: Error updating faculty', error);
          return throwError(() => new Error('Failed to update faculty'));
        })
      );
  }

  deleteFaculty(id: number): Observable<void> {
    return this.apiService.delete<void>(`${ApiPaths.FACULTIES}/${id}`).pipe(
      catchError((error) => {
        console.error('FacultyService: Error deleting faculty', error);
        return throwError(() => new Error('Failed to delete faculty'));
      })
    );
  }

  getNumOfDepartments(facultyId: number): Observable<number> {
    return this.apiService
      .get<number>(`${ApiPaths.FACULTIES}/${facultyId}/departments/count`)
      .pipe(
        catchError((error) => {
          console.error(
            'FacultyService: Error fetching department count',
            error
          );
          return throwError(() => new Error('Failed to load department count'));
        })
      );
  }
}
