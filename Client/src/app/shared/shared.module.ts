import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Button, ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { FooterComponent } from './components/footer/footer.component';
import { HeaderComponent } from './components/header/header.component';
import { CardComponent } from './components/primeng/card/card.component';
import { GalleryComponent } from './components/primeng/gallery/gallery.component';
import { PrimNgModule } from './components/primeng/prim-ng.module';
import { SectionHeadComponent } from './components/section-head/section-head.component';

@NgModule({
  declarations: [HeaderComponent, FooterComponent, SectionHeadComponent],
  imports: [CommonModule, RouterModule, PrimNgModule, Button],
  exports: [
    CommonModule,
    HeaderComponent,
    FooterComponent,
    SectionHeadComponent,
    PrimNgModule,
  ],
})
export class SharedModule {}
