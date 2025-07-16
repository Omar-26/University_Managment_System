export interface FacultyDTO {
  id: number;
  name: string;
}

export interface FacultyWithDepartments extends FacultyDTO {
  departmentCount?: number;
}
