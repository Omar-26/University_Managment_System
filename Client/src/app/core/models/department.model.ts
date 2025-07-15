export interface DepartmentDTO {
  id: number;
  name: string;
  facultyId: number;
}

export interface DepartmentWithCourses extends DepartmentDTO {
  numOfCourses: number;
}
