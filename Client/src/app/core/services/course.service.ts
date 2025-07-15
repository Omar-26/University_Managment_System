import { Injectable } from '@angular/core';
import { ApiPaths } from '@core/constants/api-paths';
import { CourseDTO } from '@core/models/course.model';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root',
})
export class CourseService {
  constructor(private apiService: ApiService) {}

  getCourses(): Observable<CourseDTO[]> {
    return this.apiService.get<CourseDTO[]>(ApiPaths.COURSES).pipe(
      map((response) => response || []),
      catchError((error) => {
        console.error('CourseService: Error fetching courses', error);
        return throwError(() => new Error('Failed to load courses'));
      })
    );
  }

  getCourse(id: number): Observable<CourseDTO> {
    return this.apiService.get<CourseDTO>(`${ApiPaths.COURSES}/${id}`).pipe(
      catchError((error) => {
        console.error('CourseService: Error fetching course', error);
        return throwError(() => new Error('Failed to load course'));
      })
    );
  }

  getCoursesByDepartment(departmentId: number): Observable<CourseDTO[]> {
    return this.apiService
      .get<CourseDTO[]>(`${ApiPaths.DEPARTMENTS}/${departmentId}/courses`)
      .pipe(
        map((response) => response || []),
        catchError((error) => {
          console.error(
            'CourseService: Error fetching courses by department',
            error
          );
          return throwError(
            () => new Error('Failed to load courses for department')
          );
        })
      );
  }

  createCourse(course: Partial<CourseDTO>): Observable<CourseDTO> {
    return this.apiService.post<CourseDTO>(ApiPaths.COURSES, course).pipe(
      catchError((error) => {
        console.error('CourseService: Error creating course', error);
        return throwError(() => new Error('Failed to create course'));
      })
    );
  }

  updateCourse(id: number, course: Partial<CourseDTO>): Observable<CourseDTO> {
    return this.apiService
      .put<CourseDTO>(`${ApiPaths.COURSES}/${id}`, course)
      .pipe(
        catchError((error) => {
          console.error('CourseService: Error updating course', error);
          return throwError(() => new Error('Failed to update course'));
        })
      );
  }

  deleteCourse(id: number): Observable<void> {
    return this.apiService.delete<void>(`${ApiPaths.COURSES}/${id}`).pipe(
      catchError((error) => {
        console.error('CourseService: Error deleting course', error);
        return throwError(() => new Error('Failed to delete course'));
      })
    );
  }
}
