import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrl: './card.component.scss',
})
export class CardComponent {
  @Input() title: string = '';
  @Input() subTitle: string = '';
  @Input() id: any = null;
  @Input() routePath: string = '';
  @Input() image: string =
    'https://primefaces.org/cdn/primeng/images/card-ng.jpg';

  constructor(private router: Router) {}

  navigateToDetails(): void {
    if (this.routePath && this.id) {
      this.router.navigate([this.routePath, this.id]);
      console.log(`Navigating to ${this.routePath}/${this.id}`);
    }
  }
}
