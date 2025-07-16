import { Injectable } from '@angular/core';
import { ApiPaths } from '@core/constants/api-paths';
import { DepartmentDTO } from '@core/models/department.model';
import { FacultyDTO } from '@core/models/faculty.model';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root',
})

/**
 * Service for managing faculty-related operations.
 * This service provides methods to fetch, create, update, and delete faculties,
 * as well as to fetch entities associated with a faculty.
 * @export
 * @class FacultyService
 * * @implements {IFacultyService}
 * * @see {@link IFacultyService} for the interface definition.
 * */
export class FacultyService {
  constructor(private apiService: ApiService) {}

  // ================================================================
  // CRUD operations
  // ================================================================

  /**
   * Fetches all faculties from the API.
   * @returns An observable of an array of FacultyDTO.
   */
  getFaculties(): Observable<FacultyDTO[]> {
    return this.apiService.get<FacultyDTO[]>(ApiPaths.FACULTIES).pipe(
      catchError((error) => {
        console.error('FacultyService: Error fetching faculties', error);
        return throwError(() => new Error('Failed to load faculties'));
      })
    );
  }

  /**
   * Fetches a specific faculty by ID.
   * @param facultyId The ID of the faculty to fetch.
   * @returns An observable of the FacultyDTO.
   */
  getFaculty(facultyId: number): Observable<FacultyDTO> {
    return this.apiService
      .get<FacultyDTO>(`${ApiPaths.FACULTIES}/${facultyId}`)
      .pipe(
        catchError((error) => {
          console.error('FacultyService: Error fetching faculty', error);
          return throwError(() => new Error('Failed to load faculty'));
        })
      );
  }

  /**
   * Creates a new faculty.
   * @param faculty The faculty data to create.
   * @returns An observable of the created FacultyDTO.
   */
  createFaculty(faculty: Partial<FacultyDTO>): Observable<FacultyDTO> {
    return this.apiService.post<FacultyDTO>(ApiPaths.FACULTIES, faculty).pipe(
      catchError((error) => {
        console.error('FacultyService: Error creating faculty', error);
        return throwError(() => new Error('Failed to create faculty'));
      })
    );
  }

  /**
   * Updates an existing faculty.
   * @param facultyId The ID of the faculty to update.
   * @param faculty The updated faculty data.
   * @returns An observable of the updated FacultyDTO.
   */
  updateFaculty(
    facultyId: number,
    faculty: Partial<FacultyDTO>
  ): Observable<FacultyDTO> {
    return this.apiService
      .put<FacultyDTO>(`${ApiPaths.FACULTIES}/${facultyId}`, faculty)
      .pipe(
        catchError((error) => {
          console.error('FacultyService: Error updating faculty', error);
          return throwError(() => new Error('Failed to update faculty'));
        })
      );
  }

  /**
   * Deletes a faculty by ID.
   * @param id The ID of the faculty to delete.
   * @returns An observable that completes when the deletion is successful.
   */
  deleteFaculty(facultyId: number): Observable<void> {
    return this.apiService
      .delete<void>(`${ApiPaths.FACULTIES}/${facultyId}`)
      .pipe(
        catchError((error) => {
          console.error('FacultyService: Error deleting faculty', error);
          return throwError(() => new Error('Failed to delete faculty'));
        })
      );
  }

  // ================================================================
  // Business logic operations
  // ================================================================

  // department related operations

  /**
   * Fetches all departments for a specific faculty.
   * @param facultyId The ID of the faculty to fetch departments for.
   * @returns An observable of an array of DepartmentDTO.
   */
  getDepartments(facultyId: number): Observable<DepartmentDTO[]> {
    return this.apiService
      .get<DepartmentDTO[]>(`${ApiPaths.FACULTIES}/${facultyId}/departments`)
      .pipe(
        catchError((error) => {
          console.error(
            'DepartmentService: Error fetching departments by faculty',
            error
          );
          return throwError(
            () => new Error('Failed to load departments for faculty')
          );
        })
      );
  }

  /**
   * Fetches the count of departments for a specific faculty.
   * @param facultyId The ID of the faculty to fetch department count for.
   * @returns An observable of the department count.
   */
  getDepartmentCount(facultyId: number): Observable<number> {
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
