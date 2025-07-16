export interface CourseDTO {
  code: string;
  name: string;
  credits: number;
  levelId: number;
  departmentId: number;
}

export interface CourseWithDetails extends CourseDTO {
  departmentName?: string;
  levelName?: string;
}
