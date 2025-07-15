import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class PhotoService {
  getImages() {
    return Promise.resolve([
      {
        itemImageSrc:
          'https://images.unsplash.com/photo-1544531585-f14f463149ec',
        thumbnailImageSrc:
          'https://images.unsplash.com/photo-1544531585-f14f463149ec',
        alt: 'Image 1',
        title: 'Title 1',
      },
      {
        itemImageSrc: 'https://images.unsplash.com/20/cambridge.JPG',
        thumbnailImageSrc: 'https://images.unsplash.com/20/cambridge.JPG',
        alt: 'Image 2',
        title: 'Title 2',
      },
      {
        itemImageSrc: 'https://primefaces.org/cdn/primeng/images/card-ng.jpg',
        thumbnailImageSrc:
          'https://primefaces.org/cdn/primeng/images/card-ng.jpg',
        alt: 'Image 3',
        title: 'Title 3',
      },
      {
        itemImageSrc: 'https://primefaces.org/cdn/primeng/images/card-ng.jpg',
        thumbnailImageSrc:
          'https://primefaces.org/cdn/primeng/images/card-ng.jpg',
        alt: 'Image 4',
        title: 'Title 4',
      },
    ]);
  }
}
