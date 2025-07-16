// In core/models/instructor.model.ts
export interface InstructorDTO {
  id: number;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  dateOfBirth?: string;
  gender?: string;
  departmentId: number;
  departmentName?: string;
  courseCodes?: string[];
}

// Extended interface for component use
export interface InstructorWithDetailsDTO extends InstructorDTO {
  courseCount?: number;
  courses?: any[]; // Will be populated with course details
}
