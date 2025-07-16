import { Injectable } from '@angular/core';
import { ApiPaths } from '@core/constants/api-paths';
import { CourseDTO } from '@core/models/course.model';
import { StudentDTO } from '@core/models/student.model';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root',
})
/**
 * Service for managing student-related operations.
 * This service provides methods to fetch, create, update, and delete students,
 * as well as to fetch entities associated with a student.
 * @export
 * @class StudentService
 * * @implements {IStudentService}
 * * @see {@link IStudentService} for the interface definition.
 * */
export class StudentService {
  constructor(private apiService: ApiService) {}

  // ================================================================
  // CRUD operations
  // ================================================================

  /**
   * Fetches all students from the API.
   * @returns An observable of an array of StudentDTO.
   */
  getStudents(): Observable<StudentDTO[]> {
    return this.apiService.get<StudentDTO[]>(ApiPaths.STUDENTS).pipe(
      catchError((error) => {
        console.error('StudentService: Error fetching students', error);
        return throwError(() => new Error('Failed to load students'));
      })
    );
  }

  /**
   * Fetches a specific student by ID.
   * @param studentId The ID of the student to fetch.
   * @returns An observable of the StudentDTO.
   */
  getStudent(studentId: number): Observable<StudentDTO> {
    return this.apiService
      .get<StudentDTO>(`${ApiPaths.STUDENTS}/${studentId}`)
      .pipe(
        catchError((error) => {
          console.error('StudentService: Error fetching student', error);
          return throwError(() => new Error('Failed to load student'));
        })
      );
  }

  /**
   * Creates a new student.
   * @param student The student data to create.
   * @returns An observable of the created StudentDTO.
   */
  createStudent(student: Partial<StudentDTO>): Observable<StudentDTO> {
    return this.apiService.post<StudentDTO>(ApiPaths.STUDENTS, student).pipe(
      catchError((error) => {
        console.error('StudentService: Error creating student', error);
        return throwError(() => new Error('Failed to create student'));
      })
    );
  }

  /**
   * Updates an existing student.
   * @param studentId The ID of the student to update.
   * @param student The updated student data.
   * @returns An observable of the updated StudentDTO.
   */
  updateStudent(
    studentId: number,
    student: Partial<StudentDTO>
  ): Observable<StudentDTO> {
    return this.apiService
      .put<StudentDTO>(`${ApiPaths.STUDENTS}/${studentId}`, student)
      .pipe(
        catchError((error) => {
          console.error('StudentService: Error updating student', error);
          return throwError(() => new Error('Failed to update student'));
        })
      );
  }

  /**
   * Deletes a student by ID.
   * @param studentId The ID of the student to delete.
   * @returns An observable that completes when the deletion is successful.
   */
  deleteStudent(studentId: number): Observable<void> {
    return this.apiService
      .delete<void>(`${ApiPaths.STUDENTS}/${studentId}`)
      .pipe(
        catchError((error) => {
          console.error('StudentService: Error deleting student', error);
          return throwError(() => new Error('Failed to delete student'));
        })
      );
  }

  // ================================================================
  // Business logic operations
  // ================================================================

  /**
   * Fetches all courses enrolled by a specific student.
   * @param studentId The ID of the student to fetch courses for.
   * @returns An observable of an array of CourseDTO.
   */
  getEnrolledCourses(studentId: number): Observable<CourseDTO[]> {
    return this.apiService
      .get<CourseDTO[]>(`${ApiPaths.STUDENTS}/${studentId}/courses`)
      .pipe(
        catchError((error) => {
          console.error(
            'StudentService: Error fetching enrolled courses',
            error
          );
          return throwError(
            () => new Error('Failed to load enrolled courses for student')
          );
        })
      );
  }

  /**
   * Fetches the count of courses enrolled by a specific student.
   * @param studentId The ID of the student to fetch course count for.
   * @returns An observable of the enrolled course count with object format.
   */
  getEnrolledCoursesCount(studentId: number): Observable<{ courseCount: number }> {
    return this.apiService
      .get<number>(`${ApiPaths.STUDENTS}/${studentId}/courses/count`)
      .pipe(
        map(count => ({ courseCount: count })),
        catchError((error) => {
          console.error(
            'StudentService: Error fetching enrolled course count',
            error
          );
          return of({ courseCount: 0 });
        })
      );
  }

  /**
   * Enrolls a student in a course.
   * @param studentId The ID of the student.
   * @param courseCode The code of the course to enroll in.
   * @returns An observable that completes when enrollment is successful.
   */
  enrollInCourse(studentId: number, courseCode: string): Observable<void> {
    return this.apiService
      .post<void>(`${ApiPaths.STUDENTS}/${studentId}/courses/${courseCode}`, {})
      .pipe(
        catchError((error) => {
          console.error('StudentService: Error enrolling in course', error);
          return throwError(() => new Error('Failed to enroll in course'));
        })
      );
  }

  /**
   * Unenrolls a student from a course.
   * @param studentId The ID of the student.
   * @param courseCode The code of the course to unenroll from.
   * @returns An observable that completes when unenrollment is successful.
   */
  unenrollFromCourse(studentId: number, courseCode: string): Observable<void> {
    return this.apiService
      .delete<void>(`${ApiPaths.STUDENTS}/${studentId}/courses/${courseCode}`)
      .pipe(
        catchError((error) => {
          console.error('StudentService: Error unenrolling from course', error);
          return throwError(() => new Error('Failed to unenroll from course'));
        })
      );
  }

  /**
   * Fetches students by level ID.
   * @param levelId The ID of the level.
   * @returns An observable of an array of StudentDTO.
   */
  getStudentsByLevel(levelId: number): Observable<StudentDTO[]> {
    return this.apiService
      .get<StudentDTO[]>(`${ApiPaths.LEVELS}/${levelId}/students`)
      .pipe(
        catchError((error) => {
          console.error('StudentService: Error fetching students by level', error);
          return throwError(() => new Error('Failed to load students by level'));
        })
      );
  }

  /**
   * Fetches students by department ID.
   * @param departmentId The ID of the department.
   * @returns An observable of an array of StudentDTO.
   */
  getStudentsByDepartment(departmentId: number): Observable<StudentDTO[]> {
    return this.apiService
      .get<StudentDTO[]>(`${ApiPaths.DEPARTMENTS}/${departmentId}/students`)
      .pipe(
        catchError((error) => {
          console.error('StudentService: Error fetching students by department', error);
          return throwError(() => new Error('Failed to load students by department'));
        })
      );
  }

  /**
   * Fetches student's academic information including GPA, credits, etc.
   * @param studentId The ID of the student.
   * @returns An observable of the student's academic info.
   */
  getStudentAcademicInfo(studentId: number): Observable<any> {
    return this.apiService
      .get<any>(`${ApiPaths.STUDENTS}/${studentId}/academic-info`)
      .pipe(
        catchError((error) => {
          console.error('StudentService: Error fetching academic info', error);
          return throwError(() => new Error('Failed to load academic information'));
        })
      );
  }

  /**
   * Fetches student's grades for all enrolled courses.
   * @param studentId The ID of the student.
   * @returns An observable of the student's grades.
   */
  getStudentGrades(studentId: number): Observable<any[]> {
    return this.apiService
      .get<any[]>(`${ApiPaths.STUDENTS}/${studentId}/grades`)
      .pipe(
        catchError((error) => {
          console.error('StudentService: Error fetching student grades', error);
          return throwError(() => new Error('Failed to load student grades'));
        })
      );
  }

  /**
   * Fetches student's schedule/timetable.
   * @param studentId The ID of the student.
   * @returns An observable of the student's schedule.
   */
  getStudentSchedule(studentId: number): Observable<any[]> {
    return this.apiService
      .get<any[]>(`${ApiPaths.STUDENTS}/${studentId}/schedule`)
      .pipe(
        catchError((error) => {
          console.error('StudentService: Error fetching student schedule', error);
          return throwError(() => new Error('Failed to load student schedule'));
        })
      );
  }
}
