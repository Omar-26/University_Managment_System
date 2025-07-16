export interface StudentDTO {
  id: number;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  dateOfBirth?: string;
  gender?: string;
  departmentId: number;
  departmentName?: string;
  levelId: number;
  levelName?: string;
  facultyId?: number;
  facultyName?: string;
}
