import { Injectable } from '@angular/core';
import { ApiPaths } from '@core/constants/api-paths';
import { CourseDTO } from '@core/models/course.model';
import { DepartmentDTO } from '@core/models/department.model';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root',
})
/**
 * Service for managing department-related operations.
 * This service provides methods to fetch, create, update, and delete departments,
 * as well as to fetch entities associated with a department.
 * @export
 * @class DepartmentService
 * * @implements {IDepartmentService}
 * * @see {@link IDepartmentService} for the interface definition.
 * */
export class DepartmentService {
  constructor(private apiService: ApiService) {}

  // ================================================================
  // CRUD operations
  // ================================================================

  /**
   * Fetches all departments from the API.
   * @returns An observable of an array of DepartmentDTO.
   */
  getDepartments(): Observable<DepartmentDTO[]> {
    return this.apiService.get<DepartmentDTO[]>(ApiPaths.DEPARTMENTS).pipe(
      catchError((error) => {
        console.error('DepartmentService: Error fetching departments', error);
        return throwError(() => new Error('Failed to load departments'));
      })
    );
  }

  /**
   * Fetches a specific department by ID.
   * @param departmentId The ID of the department to fetch.
   * @returns An observable of the DepartmentDTO.
   */
  getDepartment(departmentId: number): Observable<DepartmentDTO> {
    return this.apiService
      .get<DepartmentDTO>(`${ApiPaths.DEPARTMENTS}/${departmentId}`)
      .pipe(
        catchError((error) => {
          console.error('DepartmentService: Error fetching department', error);
          return throwError(() => new Error('Failed to load department'));
        })
      );
  }

  /**
   * Creates a new department.
   * @param department The department data to create.
   * @returns An observable of the created DepartmentDTO.
   */
  createDepartment(
    department: Partial<DepartmentDTO>
  ): Observable<DepartmentDTO> {
    return this.apiService
      .post<DepartmentDTO>(ApiPaths.DEPARTMENTS, department)
      .pipe(
        catchError((error) => {
          console.error('DepartmentService: Error creating department', error);
          return throwError(() => new Error('Failed to create department'));
        })
      );
  }

  /**
   * Updates an existing department.
   * @param departmentId The ID of the department to update.
   * @param department The updated department data.
   * @returns An observable of the updated DepartmentDTO.
   */
  updateDepartment(
    departmentId: number,
    department: Partial<DepartmentDTO>
  ): Observable<DepartmentDTO> {
    return this.apiService
      .put<DepartmentDTO>(`${ApiPaths.DEPARTMENTS}/${departmentId}`, department)
      .pipe(
        catchError((error) => {
          console.error('DepartmentService: Error updating department', error);
          return throwError(() => new Error('Failed to update department'));
        })
      );
  }

  /**
   * Deletes a department by ID.
   * @param departmentId The ID of the department to delete.
   * @returns An observable that completes when the deletion is successful.
   */
  deleteDepartment(departmentId: number): Observable<void> {
    return this.apiService
      .delete<void>(`${ApiPaths.DEPARTMENTS}/${departmentId}`)
      .pipe(
        catchError((error) => {
          console.error('DepartmentService: Error deleting department', error);
          return throwError(() => new Error('Failed to delete department'));
        })
      );
  }

  // ================================================================
  // Business logic operations
  // ================================================================

  /**
   * Fetches all courses for a specific department.
   * @param departmentId The ID of the department to fetch courses for.
   * @returns An observable of an array of CourseDTO.
   */
  getCourses(departmentId: number): Observable<CourseDTO[]> {
    return this.apiService
      .get<CourseDTO[]>(`${ApiPaths.DEPARTMENTS}/${departmentId}/courses`)
      .pipe(
        catchError((error) => {
          console.error(
            'DepartmentService: Error fetching courses by department',
            error
          );
          return throwError(
            () => new Error('Failed to load courses for department')
          );
        })
      );
  }

  /**
   * Fetches the count of courses for a specific department.
   * @param departmentId The ID of the department to fetch course count for.
   * @returns An observable of the course count.
   */
  getCoursesCount(departmentId: number): Observable<number> {
    return this.apiService
      .get<number>(`${ApiPaths.DEPARTMENTS}/${departmentId}/courses/count`)
      .pipe(
        catchError((error) => {
          console.error(
            'DepartmentService: Error fetching course count',
            error
          );
          return of(0);
        })
      );
  }
}
