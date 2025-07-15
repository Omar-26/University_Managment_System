import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { GalleriaModule } from 'primeng/galleria';
import { CardComponent } from './card/card.component';
import { GalleryComponent } from './gallery/gallery.component';

@NgModule({
  declarations: [CardComponent, GalleryComponent],
  imports: [CommonModule, ButtonModule, CardModule, GalleriaModule],
  exports: [CardComponent, GalleryComponent],
})
export class PrimNgModule {}
