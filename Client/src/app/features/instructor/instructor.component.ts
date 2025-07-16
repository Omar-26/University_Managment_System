// In instructor.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { InstructorDTO } from '@core/models/instructor.model';
import { CourseDTO } from '@core/models/course.model';
import { InstructorService } from '@core/services/instructor.service';

@Component({
  selector: 'app-instructor',
  templateUrl: './instructor.component.html',
  styleUrl: './instructor.component.scss'
})
export class InstructorComponent implements OnInit {
  instructorId!: number;
  instructor: InstructorDTO | null = null;
  assignedCourses: CourseDTO[] = [];
  instructorStudents: any[] = [];
  instructorSchedule: any[] = [];
  teachingStats: any = null;

  loading = true;
  error: string | null = null;
  activeTab = 'overview';

  // Sample data for demonstration
  upcomingClasses = [
    { time: '09:00 AM', course: 'Data Structures', room: 'Room 101', students: 25 },
    { time: '11:00 AM', course: 'Web Development', room: 'Lab 203', students: 30 },
    { time: '02:00 PM', course: 'Database Systems', room: 'Room 305', students: 22 }
  ];

  recentActivities = [
    { action: 'Graded assignments', course: 'Data Structures', time: '2 hours ago' },
    { action: 'Posted new material', course: 'Web Development', time: '1 day ago' },
    { action: 'Updated course syllabus', course: 'Database Systems', time: '2 days ago' }
  ];

  constructor(
    private instructorService: InstructorService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.instructorId = +this.route.snapshot.paramMap.get('id')!;
    this.loadInstructorData();
  }

  private loadInstructorData() {
    this.loading = true;
    this.error = null;

    this.instructorService.getInstructor(this.instructorId).subscribe({
      next: (instructor) => {
        this.instructor = instructor;
        this.loading = false;
        console.log('Instructor Data:', instructor);

        // Load additional data
        this.loadAssignedCourses();
        this.loadTeachingStats();
      },
      error: (error) => {
        this.error = 'Failed to load instructor data';
        this.loading = false;
        console.error('Error:', error);
      }
    });
  }

  private loadAssignedCourses() {
    if (this.instructor?.id) {
      this.instructorService.getAssignedCourses(this.instructor.id).subscribe({
        next: (courses) => {
          this.assignedCourses = courses;
        },
        error: (error) => {
          console.error('Error loading courses:', error);
        }
      });
    }
  }

  private loadTeachingStats() {
    if (this.instructor?.id) {
      this.instructorService.getTeachingStats(this.instructor.id).subscribe({
        next: (stats) => {
          this.teachingStats = stats;
        },
        error: (error) => {
          console.error('Error loading teaching stats:', error);
        }
      });
    }
  }

  setActiveTab(tab: string) {
    this.activeTab = tab;
  }

  getActivityIcon(action: string): string {
    if (action.includes('Graded')) return 'pi-check-circle';
    if (action.includes('Posted')) return 'pi-upload';
    if (action.includes('Updated')) return 'pi-pencil';
    return 'pi-info-circle';
  }
}
