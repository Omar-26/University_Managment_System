// In header.component.ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent {
  constructor(private router: Router) {}

  onUserButtonClick() {
    const id = localStorage.getItem('id');
    const role = localStorage.getItem('role');

    if (id && role) {
      const userId = parseInt(id);

      switch (role.toLowerCase()) {
        case 'student':
          this.router.navigate(['/student', userId]);
          break;
        case 'instructor':
          this.router.navigate(['/instructor', userId]);
          break;
        case 'admin':
          this.router.navigate(['/admin', userId]);
          break;
        default:
          console.warn('Unknown role:', role);
          this.router.navigate(['/']);
          break;
      }
    } else {
      // No user data found, redirect to login or home
      console.warn('No user data found in localStorage');
      this.router.navigate(['/login']);
    }
  }

  onLoginClick() {
    // this.router.navigate(['/login']);
    this.setUserData(2, 'student');
  }

  get isLoggedIn(): boolean {
    return !!(localStorage.getItem('id') && localStorage.getItem('role'));
  }

  get currentUser() {
    const id = localStorage.getItem('id');
    const role = localStorage.getItem('role');

    return {
      id: id ? parseInt(id) : null,
      role: role,
    };
  }

  // Helper method to set user data (you can call this from login component)
  setUserData(id: number, role: string) {
    localStorage.setItem('id', id.toString());
    localStorage.setItem('role', role);
  }

  // Helper method to clear user data
  logout() {
    localStorage.removeItem('id');
    localStorage.removeItem('role');
    this.router.navigate(['/']);
  }
}
