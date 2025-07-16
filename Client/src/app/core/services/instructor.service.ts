// In core/services/instructor.service.ts
import { Injectable } from '@angular/core';
import { ApiPaths } from '@core/constants/api-paths';
import { CourseDTO } from '@core/models/course.model';
import { InstructorDTO } from '@core/models/instructor.model';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root',
})
/**
 * Service for managing instructor-related operations.
 * This service provides methods to fetch, create, update, and delete instructors,
 * as well as to fetch entities associated with an instructor.
 * @export
 * @class InstructorService
 * * @implements {IInstructorService}
 * * @see {@link IInstructorService} for the interface definition.
 * */
export class InstructorService {
  constructor(private apiService: ApiService) {}

  // ================================================================
  // CRUD operations
  // ================================================================

  /**
   * Fetches all instructors from the API.
   * @returns An observable of an array of InstructorDTO.
   */
  getInstructors(): Observable<InstructorDTO[]> {
    return this.apiService.get<InstructorDTO[]>(ApiPaths.INSTRUCTORS).pipe(
      catchError((error) => {
        console.error('InstructorService: Error fetching instructors', error);
        return throwError(() => new Error('Failed to load instructors'));
      })
    );
  }

  /**
   * Fetches a specific instructor by ID.
   * @param instructorId The ID of the instructor to fetch.
   * @returns An observable of the InstructorDTO.
   */
  getInstructor(instructorId: number): Observable<InstructorDTO> {
    return this.apiService
      .get<InstructorDTO>(`${ApiPaths.INSTRUCTORS}/${instructorId}`)
      .pipe(
        catchError((error) => {
          console.error('InstructorService: Error fetching instructor', error);
          return throwError(() => new Error('Failed to load instructor'));
        })
      );
  }

  /**
   * Creates a new instructor.
   * @param instructor The instructor data to create.
   * @returns An observable of the created InstructorDTO.
   */
  createInstructor(instructor: Partial<InstructorDTO>): Observable<InstructorDTO> {
    return this.apiService.post<InstructorDTO>(ApiPaths.INSTRUCTORS, instructor).pipe(
      catchError((error) => {
        console.error('InstructorService: Error creating instructor', error);
        return throwError(() => new Error('Failed to create instructor'));
      })
    );
  }

  /**
   * Updates an existing instructor.
   * @param instructorId The ID of the instructor to update.
   * @param instructor The updated instructor data.
   * @returns An observable of the updated InstructorDTO.
   */
  updateInstructor(
    instructorId: number,
    instructor: Partial<InstructorDTO>
  ): Observable<InstructorDTO> {
    return this.apiService
      .put<InstructorDTO>(`${ApiPaths.INSTRUCTORS}/${instructorId}`, instructor)
      .pipe(
        catchError((error) => {
          console.error('InstructorService: Error updating instructor', error);
          return throwError(() => new Error('Failed to update instructor'));
        })
      );
  }

  /**
   * Deletes an instructor by ID.
   * @param instructorId The ID of the instructor to delete.
   * @returns An observable that completes when the deletion is successful.
   */
  deleteInstructor(instructorId: number): Observable<void> {
    return this.apiService
      .delete<void>(`${ApiPaths.INSTRUCTORS}/${instructorId}`)
      .pipe(
        catchError((error) => {
          console.error('InstructorService: Error deleting instructor', error);
          return throwError(() => new Error('Failed to delete instructor'));
        })
      );
  }

  // ================================================================
  // Business logic operations
  // ================================================================

  /**
   * Fetches all courses taught by a specific instructor.
   * @param instructorId The ID of the instructor to fetch courses for.
   * @returns An observable of an array of CourseDTO.
   */
  getAssignedCourses(instructorId: number): Observable<CourseDTO[]> {
    return this.apiService
      .get<CourseDTO[]>(`${ApiPaths.INSTRUCTORS}/${instructorId}/courses`)
      .pipe(
        catchError((error) => {
          console.error(
            'InstructorService: Error fetching assigned courses',
            error
          );
          return throwError(
            () => new Error('Failed to load assigned courses for instructor')
          );
        })
      );
  }

  /**
   * Fetches the count of courses assigned to a specific instructor.
   * @param instructorId The ID of the instructor to fetch course count for.
   * @returns An observable of the assigned course count with object format.
   */
  getAssignedCoursesCount(instructorId: number): Observable<{ courseCount: number }> {
    return this.apiService
      .get<number>(`${ApiPaths.INSTRUCTORS}/${instructorId}/courses/count`)
      .pipe(
        map(count => ({ courseCount: count })),
        catchError((error) => {
          console.error(
            'InstructorService: Error fetching assigned course count',
            error
          );
          return of({ courseCount: 0 });
        })
      );
  }

  /**
   * Assigns a course to an instructor.
   * @param instructorId The ID of the instructor.
   * @param courseCode The code of the course to assign.
   * @returns An observable that completes when assignment is successful.
   */
  assignCourse(instructorId: number, courseCode: string): Observable<void> {
    return this.apiService
      .post<void>(`${ApiPaths.INSTRUCTORS}/${instructorId}/courses/${courseCode}`, {})
      .pipe(
        catchError((error) => {
          console.error('InstructorService: Error assigning course', error);
          return throwError(() => new Error('Failed to assign course'));
        })
      );
  }

  /**
   * Removes a course assignment from an instructor.
   * @param instructorId The ID of the instructor.
   * @param courseCode The code of the course to unassign.
   * @returns An observable that completes when unassignment is successful.
   */
  unassignCourse(instructorId: number, courseCode: string): Observable<void> {
    return this.apiService
      .delete<void>(`${ApiPaths.INSTRUCTORS}/${instructorId}/courses/${courseCode}`)
      .pipe(
        catchError((error) => {
          console.error('InstructorService: Error unassigning course', error);
          return throwError(() => new Error('Failed to unassign course'));
        })
      );
  }

  /**
   * Fetches instructors by department ID.
   * @param departmentId The ID of the department.
   * @returns An observable of an array of InstructorDTO.
   */
  getInstructorsByDepartment(departmentId: number): Observable<InstructorDTO[]> {
    return this.apiService
      .get<InstructorDTO[]>(`${ApiPaths.DEPARTMENTS}/${departmentId}/instructors`)
      .pipe(
        catchError((error) => {
          console.error('InstructorService: Error fetching instructors by department', error);
          return throwError(() => new Error('Failed to load instructors by department'));
        })
      );
  }

  /**
   * Fetches instructor's students for all assigned courses.
   * @param instructorId The ID of the instructor.
   * @returns An observable of the instructor's students.
   */
  getInstructorStudents(instructorId: number): Observable<any[]> {
    return this.apiService
      .get<any[]>(`${ApiPaths.INSTRUCTORS}/${instructorId}/students`)
      .pipe(
        catchError((error) => {
          console.error('InstructorService: Error fetching instructor students', error);
          return throwError(() => new Error('Failed to load instructor students'));
        })
      );
  }

  /**
   * Fetches instructor's schedule/timetable.
   * @param instructorId The ID of the instructor.
   * @returns An observable of the instructor's schedule.
   */
  getInstructorSchedule(instructorId: number): Observable<any[]> {
    return this.apiService
      .get<any[]>(`${ApiPaths.INSTRUCTORS}/${instructorId}/schedule`)
      .pipe(
        catchError((error) => {
          console.error('InstructorService: Error fetching instructor schedule', error);
          return throwError(() => new Error('Failed to load instructor schedule'));
        })
      );
  }

  /**
   * Fetches instructor's teaching statistics.
   * @param instructorId The ID of the instructor.
   * @returns An observable of the instructor's teaching stats.
   */
  getTeachingStats(instructorId: number): Observable<any> {
    return this.apiService
      .get<any>(`${ApiPaths.INSTRUCTORS}/${instructorId}/stats`)
      .pipe(
        catchError((error) => {
          console.error('InstructorService: Error fetching teaching stats', error);
          return throwError(() => new Error('Failed to load teaching statistics'));
        })
      );
  }
}
