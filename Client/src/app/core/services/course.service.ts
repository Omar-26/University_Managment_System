import { Injectable } from '@angular/core';
import { ApiPaths } from '@core/constants/api-paths';
import { CourseDTO } from '@core/models/course.model';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root',
})
/**
 * Service for managing course-related operations.
 * This service provides methods to fetch, create, update, and delete courses,
 * as well as to fetch entities associated with a course.
 * @export
 * @class CourseService
 * * @implements {ICourseService}
 * * @see {@link ICourseService} for the interface definition.
 * */
export class CourseService {
  constructor(private apiService: ApiService) {}

  // ================================================================
  // CRUD operations
  // ================================================================

  /**
   * Fetches all courses from the API.
   * @returns An observable of an array of CourseDTO.
   */
  getCourses(): Observable<CourseDTO[]> {
    return this.apiService.get<CourseDTO[]>(ApiPaths.COURSES).pipe(
      catchError((error) => {
        console.error('CourseService: Error fetching courses', error);
        return throwError(() => new Error('Failed to load courses'));
      })
    );
  }

  /**
   * Fetches a specific course by code.
   * @param courseCode The code of the course to fetch.
   * @returns An observable of the CourseDTO.
   */
  getCourse(courseCode: string): Observable<CourseDTO> {
    return this.apiService
      .get<CourseDTO>(`${ApiPaths.COURSES}/${courseCode}`)
      .pipe(
        catchError((error) => {
          console.error('CourseService: Error fetching course', error);
          return throwError(() => new Error('Failed to load course'));
        })
      );
  }

  /**
   * Creates a new course.
   * @param course The course data to create.
   * @returns An observable of the created CourseDTO.
   */
  createCourse(course: Partial<CourseDTO>): Observable<CourseDTO> {
    return this.apiService.post<CourseDTO>(ApiPaths.COURSES, course).pipe(
      catchError((error) => {
        console.error('CourseService: Error creating course', error);
        return throwError(() => new Error('Failed to create course'));
      })
    );
  }

  /**
   * Updates an existing course.
   * @param courseCode The code of the course to update.
   * @param course The updated course data.
   * @returns An observable of the updated CourseDTO.
   */
  updateCourse(
    courseCode: string,
    course: Partial<CourseDTO>
  ): Observable<CourseDTO> {
    return this.apiService
      .put<CourseDTO>(`${ApiPaths.COURSES}/${courseCode}`, course)
      .pipe(
        catchError((error) => {
          console.error('CourseService: Error updating course', error);
          return throwError(() => new Error('Failed to update course'));
        })
      );
  }

  /**
   * Deletes a course by code.
   * @param courseCode The code of the course to delete.
   * @returns An observable that completes when the deletion is successful.
   */
  deleteCourse(courseCode: string): Observable<void> {
    return this.apiService
      .delete<void>(`${ApiPaths.COURSES}/${courseCode}`)
      .pipe(
        catchError((error) => {
          console.error('CourseService: Error deleting course', error);
          return throwError(() => new Error('Failed to delete course'));
        })
      );
  }

  // ================================================================
  // Business logic operations
  // ================================================================
}
