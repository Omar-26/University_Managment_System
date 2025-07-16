// In core/services/enrollment.service.ts
import { Injectable } from '@angular/core';
import { ApiPaths } from '@core/constants/api-paths';
import {
  CourseEnrollmentDTO,
  EnrollmentDTO,
  StudentEnrollmentDTO,
} from '@core/models/enrollment.model';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root',
})
/**
 * Service for managing enrollment-related operations.
 * This service provides methods to fetch, create, update, and delete enrollments,
 * as well as to manage student-course relationships.
 * @export
 * @class EnrollmentService
 * * @implements {IEnrollmentService}
 * * @see {@link IEnrollmentService} for the interface definition.
 * */
export class EnrollmentService {
  constructor(private apiService: ApiService) {}

  // ================================================================
  // CRUD operations
  // ================================================================

  /**
   * Fetches all enrollments from the API.
   * @returns An observable of an array of EnrollmentDTO.
   */
  getEnrollments(): Observable<EnrollmentDTO[]> {
    return this.apiService.get<EnrollmentDTO[]>(ApiPaths.ENROLLMENTS).pipe(
      catchError((error) => {
        console.error('EnrollmentService: Error fetching enrollments', error);
        return throwError(() => new Error('Failed to load enrollments'));
      })
    );
  }

  /**
   * Fetches a specific enrollment by ID.
   * @param enrollmentId The ID of the enrollment to fetch.
   * @returns An observable of the EnrollmentDTO.
   */
  getEnrollment(enrollmentId: number): Observable<EnrollmentDTO> {
    return this.apiService
      .get<EnrollmentDTO>(`${ApiPaths.ENROLLMENTS}/${enrollmentId}`)
      .pipe(
        catchError((error) => {
          console.error('EnrollmentService: Error fetching enrollment', error);
          return throwError(() => new Error('Failed to load enrollment'));
        })
      );
  }

  /**
   * Creates a new enrollment.
   * @param enrollment The enrollment data to create.
   * @returns An observable of the created EnrollmentDTO.
   */
  createEnrollment(
    enrollment: Partial<EnrollmentDTO>
  ): Observable<EnrollmentDTO> {
    return this.apiService
      .post<EnrollmentDTO>(ApiPaths.ENROLLMENTS, enrollment)
      .pipe(
        catchError((error) => {
          console.error('EnrollmentService: Error creating enrollment', error);
          return throwError(() => new Error('Failed to create enrollment'));
        })
      );
  }

  /**
   * Updates an existing enrollment.
   * @param enrollmentId The ID of the enrollment to update.
   * @param enrollment The updated enrollment data.
   * @returns An observable of the updated EnrollmentDTO.
   */
  updateEnrollment(
    enrollmentId: number,
    enrollment: Partial<EnrollmentDTO>
  ): Observable<EnrollmentDTO> {
    return this.apiService
      .put<EnrollmentDTO>(`${ApiPaths.ENROLLMENTS}/${enrollmentId}`, enrollment)
      .pipe(
        catchError((error) => {
          console.error('EnrollmentService: Error updating enrollment', error);
          return throwError(() => new Error('Failed to update enrollment'));
        })
      );
  }

  /**
   * Deletes an enrollment by ID.
   * @param enrollmentId The ID of the enrollment to delete.
   * @returns An observable that completes when the deletion is successful.
   */
  deleteEnrollment(enrollmentId: number): Observable<void> {
    return this.apiService
      .delete<void>(`${ApiPaths.ENROLLMENTS}/${enrollmentId}`)
      .pipe(
        catchError((error) => {
          console.error('EnrollmentService: Error deleting enrollment', error);
          return throwError(() => new Error('Failed to delete enrollment'));
        })
      );
  }

  // ================================================================
  // Student-specific enrollment operations
  // ================================================================

  /**
   * Fetches all enrollments for a specific student.
   * @param studentId The ID of the student to fetch enrollments for.
   * @returns An observable of an array of StudentEnrollmentDTO.
   */
  getStudentEnrollments(studentId: number): Observable<StudentEnrollmentDTO[]> {
    return this.apiService
      .get<StudentEnrollmentDTO[]>(
        `${ApiPaths.STUDENTS}/${studentId}/enrollments`
      )
      .pipe(
        catchError((error) => {
          console.error(
            'EnrollmentService: Error fetching student enrollments',
            error
          );
          return throwError(
            () => new Error('Failed to load enrollments for student')
          );
        })
      );
  }

  /**
   * Fetches the count of enrollments for a specific student.
   * @param studentId The ID of the student to fetch enrollment count for.
   * @returns An observable of the enrollment count with object format.
   */
  getStudentEnrollmentCount(
    studentId: number
  ): Observable<{ enrollmentCount: number }> {
    return this.apiService
      .get<number>(`${ApiPaths.STUDENTS}/${studentId}/enrollments/count`)
      .pipe(
        map((count) => ({ enrollmentCount: count })),
        catchError((error) => {
          console.error(
            'EnrollmentService: Error fetching student enrollment count',
            error
          );
          return of({ enrollmentCount: 0 });
        })
      );
  }

  /**
   * Enrolls a student in a course.
   * @param studentId The ID of the student.
   * @param courseId The ID of the course to enroll in.
   * @param enrollmentData Additional enrollment data.
   * @returns An observable of the created EnrollmentDTO.
   */
  enrollStudent(
    studentId: number,
    courseId: number,
    enrollmentData?: Partial<EnrollmentDTO>
  ): Observable<EnrollmentDTO> {
    const enrollment = {
      studentId,
      courseId,
      enrollmentDate: new Date().toISOString().split('T')[0],
      status: 'ENROLLED' as const,
      ...enrollmentData,
    };

    return this.apiService
      .post<EnrollmentDTO>(
        `${ApiPaths.STUDENTS}/${studentId}/enroll/${courseId}`,
        enrollment
      )
      .pipe(
        catchError((error) => {
          console.error('EnrollmentService: Error enrolling student', error);
          return throwError(
            () => new Error('Failed to enroll student in course')
          );
        })
      );
  }

  /**
   * Unenrolls a student from a course.
   * @param studentId The ID of the student.
   * @param courseId The ID of the course to unenroll from.
   * @returns An observable that completes when unenrollment is successful.
   */
  unenrollStudent(studentId: number, courseId: number): Observable<void> {
    return this.apiService
      .delete<void>(`${ApiPaths.STUDENTS}/${studentId}/unenroll/${courseId}`)
      .pipe(
        catchError((error) => {
          console.error('EnrollmentService: Error unenrolling student', error);
          return throwError(
            () => new Error('Failed to unenroll student from course')
          );
        })
      );
  }

  // ================================================================
  // Course-specific enrollment operations
  // ================================================================

  /**
   * Fetches all enrollments for a specific course.
   * @param courseId The ID of the course to fetch enrollments for.
   * @returns An observable of an array of CourseEnrollmentDTO.
   */
  getCourseEnrollments(courseId: number): Observable<CourseEnrollmentDTO[]> {
    return this.apiService
      .get<CourseEnrollmentDTO[]>(`${ApiPaths.COURSES}/${courseId}/enrollments`)
      .pipe(
        catchError((error) => {
          console.error(
            'EnrollmentService: Error fetching course enrollments',
            error
          );
          return throwError(
            () => new Error('Failed to load enrollments for course')
          );
        })
      );
  }

  /**
   * Fetches the count of enrollments for a specific course.
   * @param courseId The ID of the course to fetch enrollment count for.
   * @returns An observable of the enrollment count with object format.
   */
  getCourseEnrollmentCount(
    courseId: number
  ): Observable<{ enrollmentCount: number }> {
    return this.apiService
      .get<number>(`${ApiPaths.COURSES}/${courseId}/enrollments/count`)
      .pipe(
        map((count) => ({ enrollmentCount: count })),
        catchError((error) => {
          console.error(
            'EnrollmentService: Error fetching course enrollment count',
            error
          );
          return of({ enrollmentCount: 0 });
        })
      );
  }

  // ================================================================
  // Grade management operations
  // ================================================================

  /**
   * Updates the grade for a specific enrollment.
   * @param enrollmentId The ID of the enrollment to update grade for.
   * @param grade The new grade.
   * @returns An observable of the updated EnrollmentDTO.
   */
  updateEnrollmentGrade(
    enrollmentId: number,
    grade: string
  ): Observable<EnrollmentDTO> {
    return this.apiService
      .put<EnrollmentDTO>(`${ApiPaths.ENROLLMENTS}/${enrollmentId}/grade`, {
        grade,
      })
      .pipe(
        catchError((error) => {
          console.error(
            'EnrollmentService: Error updating enrollment grade',
            error
          );
          return throwError(
            () => new Error('Failed to update enrollment grade')
          );
        })
      );
  }

  /**
   * Updates the status for a specific enrollment.
   * @param enrollmentId The ID of the enrollment to update status for.
   * @param status The new status.
   * @returns An observable of the updated EnrollmentDTO.
   */
  updateEnrollmentStatus(
    enrollmentId: number,
    status: EnrollmentDTO['status']
  ): Observable<EnrollmentDTO> {
    return this.apiService
      .put<EnrollmentDTO>(`${ApiPaths.ENROLLMENTS}/${enrollmentId}/status`, {
        status,
      })
      .pipe(
        catchError((error) => {
          console.error(
            'EnrollmentService: Error updating enrollment status',
            error
          );
          return throwError(
            () => new Error('Failed to update enrollment status')
          );
        })
      );
  }

  // ================================================================
  // Bulk operations
  // ================================================================

  /**
   * Enrolls multiple students in a course.
   * @param courseId The ID of the course.
   * @param studentIds Array of student IDs to enroll.
   * @returns An observable of the created enrollments.
   */
  bulkEnrollStudents(
    courseId: number,
    studentIds: number[]
  ): Observable<EnrollmentDTO[]> {
    return this.apiService
      .post<EnrollmentDTO[]>(`${ApiPaths.COURSES}/${courseId}/bulk-enroll`, {
        studentIds,
      })
      .pipe(
        catchError((error) => {
          console.error(
            'EnrollmentService: Error bulk enrolling students',
            error
          );
          return throwError(() => new Error('Failed to bulk enroll students'));
        })
      );
  }

  /**
   * Updates grades for multiple enrollments.
   * @param gradeUpdates Array of enrollment ID and grade pairs.
   * @returns An observable of the updated enrollments.
   */
  bulkUpdateGrades(
    gradeUpdates: { enrollmentId: number; grade: string }[]
  ): Observable<EnrollmentDTO[]> {
    return this.apiService
      .put<EnrollmentDTO[]>(`${ApiPaths.ENROLLMENTS}/bulk-grade`, {
        gradeUpdates,
      })
      .pipe(
        catchError((error) => {
          console.error('EnrollmentService: Error bulk updating grades', error);
          return throwError(() => new Error('Failed to bulk update grades'));
        })
      );
  }

  // ================================================================
  // Search and filter operations
  // ================================================================

  /**
   * Searches enrollments by various criteria.
   * @param searchParams Search parameters.
   * @returns An observable of matching enrollments.
   */
  searchEnrollments(searchParams: {
    studentName?: string;
    courseName?: string;
    status?: string;
    semester?: string;
    academicYear?: string;
  }): Observable<EnrollmentDTO[]> {
    return this.apiService
      .get<EnrollmentDTO[]>(`${ApiPaths.ENROLLMENTS}/search`, {
        params: searchParams,
      })
      .pipe(
        catchError((error) => {
          console.error(
            'EnrollmentService: Error searching enrollments',
            error
          );
          return throwError(() => new Error('Failed to search enrollments'));
        })
      );
  }

  /**
   * Fetches enrollments by status.
   * @param status The enrollment status to filter by.
   * @returns An observable of enrollments with the specified status.
   */
  getEnrollmentsByStatus(
    status: EnrollmentDTO['status']
  ): Observable<EnrollmentDTO[]> {
    return this.apiService
      .get<EnrollmentDTO[]>(`${ApiPaths.ENROLLMENTS}/status/${status}`)
      .pipe(
        catchError((error) => {
          console.error(
            'EnrollmentService: Error fetching enrollments by status',
            error
          );
          return throwError(
            () => new Error('Failed to load enrollments by status')
          );
        })
      );
  }

  /**
   * Fetches enrollments by semester and academic year.
   * @param semester The semester to filter by.
   * @param academicYear The academic year to filter by.
   * @returns An observable of enrollments for the specified period.
   */
  getEnrollmentsBySemester(
    semester: string,
    academicYear: string
  ): Observable<EnrollmentDTO[]> {
    return this.apiService
      .get<EnrollmentDTO[]>(
        `${ApiPaths.ENROLLMENTS}/semester/${semester}/${academicYear}`
      )
      .pipe(
        catchError((error) => {
          console.error(
            'EnrollmentService: Error fetching enrollments by semester',
            error
          );
          return throwError(
            () => new Error('Failed to load enrollments by semester')
          );
        })
      );
  }
}
