export interface EnrollmentDTO {
  studentId: number;
  courseCode: string;
  grade: number;
}

export interface EnrollmentWithDetails extends EnrollmentDTO {
  studentName?: string;
  courseName?: string;
  courseCredits?: number;
  courseLevelId?: number;
  courseDepartmentId?: number;
}

// // In core/models/enrollment.model.ts
// export interface EnrollmentDTO {
//   id?: number;
//   studentId: number;
//   courseId: number;
//   enrollmentDate: string; // LocalDate comes as string from backend
//   status: 'ENROLLED' | 'COMPLETED' | 'DROPPED' | 'WITHDRAWN';
//   grade?: string;
//   credits?: number;
//   semester?: string;
//   academicYear?: string;
// }

// Extended interfaces for component use
export interface StudentEnrollmentDTO extends EnrollmentDTO {
  studentName?: string;
  courseName?: string;
  courseCode: string;
}

export interface CourseEnrollmentDTO extends EnrollmentDTO {
  studentName?: string;
  studentNumber?: string;
}

export interface EnrollmentWithDetailsDTO extends EnrollmentDTO {
  studentName?: string;
  courseName?: string;
  courseCode: string;
  departmentName?: string;
  instructorName?: string;
}
