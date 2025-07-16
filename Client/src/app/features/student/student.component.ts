// In student.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { StudentDTO } from '@core/models/student.model';
import { CourseDTO } from '@core/models/course.model';
import { StudentService } from '@core/services/student.service';

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrl: './student.component.scss',
})
export class StudentComponent implements OnInit {
  studentId!: number;
  student: StudentDTO | null = null; // Simplified since backend returns all data
  enrolledCourses: CourseDTO[] = [];
  studentGrades: any[] = [];
  studentSchedule: any[] = [];
  academicInfo: any = null;

  loading = true;
  error: string | null = null;
  activeTab = 'overview';

  // Sample data for demonstration
  upcomingClasses = [
    { time: '09:00 AM', course: 'Data Structures', room: 'Room 101', instructor: 'Dr. Smith' },
    { time: '11:00 AM', course: 'Web Development', room: 'Lab 203', instructor: 'Prof. Johnson' },
    { time: '02:00 PM', course: 'Database Systems', room: 'Room 305', instructor: 'Dr. Brown' }
  ];

  recentGrades = [
    { course: 'Data Structures', assignment: 'Midterm Exam', grade: 'A-', date: '2024-01-15' },
    { course: 'Web Development', assignment: 'Project 1', grade: 'B+', date: '2024-01-10' },
    { course: 'Database Systems', assignment: 'Quiz 2', grade: 'A', date: '2024-01-08' }
  ];

  constructor(
    private studentService: StudentService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.studentId = +this.route.snapshot.paramMap.get('id')!;
    this.loadStudentData();
  }

  private loadStudentData() {
    this.loading = true;
    this.error = null;

    // Since backend returns all data, we only need one call
    this.studentService.getStudent(this.studentId).subscribe({
      next: (student) => {
        this.student = student;
        this.loading = false;
        console.log('Student Data:', student);

        // Load additional data if needed
        this.loadEnrolledCourses();
        this.loadAcademicInfo();
      },
      error: (error) => {
        this.error = 'Failed to load student data';
        this.loading = false;
        console.error('Error:', error);
      }
    });
  }

  private loadEnrolledCourses() {
    if (this.student?.id) {
      this.studentService.getEnrolledCourses(this.student.id).subscribe({
        next: (courses) => {
          this.enrolledCourses = courses;
        },
        error: (error) => {
          console.error('Error loading courses:', error);
        }
      });
    }
  }

  private loadAcademicInfo() {
    if (this.student?.id) {
      this.studentService.getStudentAcademicInfo(this.student.id).subscribe({
        next: (info) => {
          this.academicInfo = info;
        },
        error: (error) => {
          console.error('Error loading academic info:', error);
        }
      });
    }
  }

  setActiveTab(tab: string) {
    this.activeTab = tab;
  }

  getGradeClass(grade: string): string {
    if (grade.startsWith('A')) return 'grade-a';
    if (grade.startsWith('B')) return 'grade-b';
    if (grade.startsWith('C')) return 'grade-c';
    return 'grade-other';
  }
}
