import { Injectable } from '@angular/core';
import { ApiPaths } from '@core/constants/api-paths';
import { CourseDTO } from '@core/models/course.model';
import { LevelDTO } from '@core/models/level.model';
import { StudentDTO } from '@core/models/student.model';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root',
})
/**
 * Service for managing level-related operations.
 * This service provides methods to fetch, create, update, and delete levels,
 * as well as to fetch entities associated with a level.
 * @export
 * @class LevelService
 * * @implements {ILevelService}
 * * @see {@link ILevelService} for the interface definition.
 * */
export class LevelService {
  constructor(private apiService: ApiService) {}

  // ================================================================
  // CRUD operations
  // ================================================================

  /**
   * Fetches all levels from the API.
   * @returns An observable of an array of LevelDTO.
   */
  getLevels(): Observable<LevelDTO[]> {
    return this.apiService.get<LevelDTO[]>(ApiPaths.LEVELS).pipe(
      catchError((error) => {
        console.error('LevelService: Error fetching levels', error);
        return throwError(() => new Error('Failed to load levels'));
      })
    );
  }

  /**
   * Fetches a specific level by ID.
   * @param levelId The ID of the level to fetch.
   * @returns An observable of the LevelDTO.
   */
  getLevel(levelId: number): Observable<LevelDTO> {
    return this.apiService.get<LevelDTO>(`${ApiPaths.LEVELS}/${levelId}`).pipe(
      catchError((error) => {
        console.error('LevelService: Error fetching level', error);
        return throwError(() => new Error('Failed to load level'));
      })
    );
  }

  /**
   * Creates a new level.
   * @param level The level data to create.
   * @returns An observable of the created LevelDTO.
   */
  createLevel(level: Partial<LevelDTO>): Observable<LevelDTO> {
    return this.apiService.post<LevelDTO>(ApiPaths.LEVELS, level).pipe(
      catchError((error) => {
        console.error('LevelService: Error creating level', error);
        return throwError(() => new Error('Failed to create level'));
      })
    );
  }

  /**
   * Updates an existing level.
   * @param levelId The ID of the level to update.
   * @param level The updated level data.
   * @returns An observable of the updated LevelDTO.
   */
  updateLevel(levelId: number, level: Partial<LevelDTO>): Observable<LevelDTO> {
    return this.apiService
      .put<LevelDTO>(`${ApiPaths.LEVELS}/${levelId}`, level)
      .pipe(
        catchError((error) => {
          console.error('LevelService: Error updating level', error);
          return throwError(() => new Error('Failed to update level'));
        })
      );
  }

  /**
   * Deletes a level by ID.
   * @param levelId The ID of the level to delete.
   * @returns An observable that completes when the deletion is successful.
   */
  deleteLevel(levelId: number): Observable<void> {
    return this.apiService.delete<void>(`${ApiPaths.LEVELS}/${levelId}`).pipe(
      catchError((error) => {
        console.error('LevelService: Error deleting level', error);
        return throwError(() => new Error('Failed to delete level'));
      })
    );
  }

  // ================================================================
  // Business logic operations
  // ================================================================

  // Student related operations

  /**
   * Fetches all students associated with a specific level.
   * @param levelId The ID of the level.
   * @returns An observable of an array of StudentDTO.
   */
  getStudents(levelId: number): Observable<StudentDTO[]> {
    return this.apiService
      .get<StudentDTO[]>(`${ApiPaths.LEVELS}/${levelId}/students`)
      .pipe(
        catchError((error) => {
          console.error(
            'LevelService: Error fetching students by level',
            error
          );
          return throwError(
            () => new Error('Failed to load students for level')
          );
        })
      );
  }

  /**
   * Counts all students associated with a specific level.
   * @param levelId The ID of the level.
   * @returns An observable of the student count with object format.
   */
  getStudentCount(levelId: number): Observable<{ studentCount: number }> {
    return this.apiService
      .get<number>(`${ApiPaths.LEVELS}/${levelId}/students/count`)
      .pipe(
        map((count) => ({ studentCount: count })),
        catchError((error) => {
          console.error('LevelService: Error fetching student count', error);
          return of({ studentCount: 0 });
        })
      );
  }

  // Course related operations

  /**
   * Fetches all courses associated with a specific level.
   * @param levelId The ID of the level.
   * @returns An observable of an array of CourseDTO.
   */
  getCourses(levelId: number): Observable<CourseDTO[]> {
    return this.apiService
      .get<CourseDTO[]>(`${ApiPaths.LEVELS}/${levelId}/courses`)
      .pipe(
        catchError((error) => {
          console.error('LevelService: Error fetching courses by level', error);
          return throwError(
            () => new Error('Failed to load courses for level')
          );
        })
      );
  }

  /**
   * Counts all courses associated with a specific level.
   * @param levelId The ID of the level.
   * @returns An observable of the course count with object format.
   */
  getCourseCount(levelId: number): Observable<{ courseCount: number }> {
    return this.apiService
      .get<number>(`${ApiPaths.LEVELS}/${levelId}/courses/count`)
      .pipe(
        map((count) => ({ courseCount: count })),
        catchError((error) => {
          console.error('LevelService: Error fetching course count', error);
          return of({ courseCount: 0 });
        })
      );
  }
}
