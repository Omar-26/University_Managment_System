import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-section-head',
  templateUrl: './section-head.component.html',
  styleUrl: './section-head.component.scss',
})
export class SectionHeadComponent {
  @Input() title: string = '';
  @Input() addBtnText: string = '';
  @Input() showBtnPath: string = '';
  @Input() addBtnPath: string = '';
}
