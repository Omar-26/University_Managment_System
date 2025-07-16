import {
  AfterViewInit,
  Component,
  ElementRef,
  OnInit,
  QueryList,
  ViewChildren,
} from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit, AfterViewInit {
  @ViewChildren('funFactItem') funFactItems!: QueryList<ElementRef>;

  funFacts = [
    { count: 0, target: 60, label: 'TEACHERS', animated: false },
    { count: 0, target: 50, label: 'COURSES', animated: false },
    { count: 0, target: 1000, label: 'STUDENTS', animated: false },
    { count: 0, target: 3737, label: 'SATISFIED CLIENTS', animated: false },
  ];

  ngOnInit() {}

  ngAfterViewInit() {
    this.setupAnimation();
  }

  private setupAnimation() {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            // Start counting animation
            this.startCountingAnimation();
            // Disconnect observer so it only runs once
            observer.disconnect();
          }
        });
      },
      {
        threshold: 0.3, // Trigger when 30% visible
        rootMargin: '0px',
      }
    );

    // Observe the entire section
    const section = document.querySelector('.bg-primary');
    if (section) {
      observer.observe(section);
    }
  }

  private startCountingAnimation() {
    // Add slide-in animation
    this.funFactItems.forEach((item, index) => {
      setTimeout(() => {
        item.nativeElement.classList.add('animate');
      }, index * 100);
    });

    this.funFacts.forEach((fact, index) => {
      if (fact.animated) return;

      fact.animated = true;

      setTimeout(() => {
        const duration = 1500; // 2 seconds
        const steps = 60; // Number of steps
        const increment = fact.target / steps;
        let currentStep = 0;

        const timer = setInterval(() => {
          currentStep++;
          fact.count = Math.floor(increment * currentStep);

          if (currentStep >= steps) {
            fact.count = fact.target;
            clearInterval(timer);
          }
        }, duration / steps);
      }, index * 200);
    });
  }
}
