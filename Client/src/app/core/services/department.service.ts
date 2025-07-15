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
export class DepartmentService {
  constructor(private apiService: ApiService) {}

  getDepartments(): Observable<DepartmentDTO[]> {
    return this.apiService.get<DepartmentDTO[]>(ApiPaths.DEPARTMENTS).pipe(
      map((response) => response || []),
      catchError((error) => {
        console.error('DepartmentService: Error fetching departments', error);
        return throwError(() => new Error('Failed to load departments'));
      })
    );
  }

  getDepartment(id: number): Observable<DepartmentDTO> {
    return this.apiService
      .get<DepartmentDTO>(`${ApiPaths.DEPARTMENTS}/${id}`)
      .pipe(
        catchError((error) => {
          console.error('DepartmentService: Error fetching department', error);
          return throwError(() => new Error('Failed to load department'));
        })
      );
  }

  getDepartmentsByFaculty(facultyId: number): Observable<DepartmentDTO[]> {
    return this.apiService
      .get<DepartmentDTO[]>(`${ApiPaths.FACULTIES}/${facultyId}/departments`)
      .pipe(
        map((response) => response || []),
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

  updateDepartment(
    id: number,
    department: Partial<DepartmentDTO>
  ): Observable<DepartmentDTO> {
    return this.apiService
      .put<DepartmentDTO>(`${ApiPaths.DEPARTMENTS}/${id}`, department)
      .pipe(
        catchError((error) => {
          console.error('DepartmentService: Error updating department', error);
          return throwError(() => new Error('Failed to update department'));
        })
      );
  }

  deleteDepartment(id: number): Observable<void> {
    return this.apiService.delete<void>(`${ApiPaths.DEPARTMENTS}/${id}`).pipe(
      catchError((error) => {
        console.error('DepartmentService: Error deleting department', error);
        return throwError(() => new Error('Failed to delete department'));
      })
    );
  }

  getCoursesCount(departmentId: number): Observable<{ numOfCourses: number }> {
    return this.apiService
      .get<number>(`${ApiPaths.DEPARTMENTS}/${departmentId}/courses/count`)
      .pipe(
        map((count) => ({ numOfCourses: count || 0 })),
        catchError((error) => {
          console.error(
            'DepartmentService: Error fetching course count',
            error
          );
          return of({ numOfCourses: 0 });
        })
      );
  }
}
