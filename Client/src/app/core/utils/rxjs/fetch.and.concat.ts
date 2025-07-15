import { Observable, forkJoin, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

/**
 * Generic helper: fetch a base list of entities, then fetch and merge extra data for each entity.
 *
 * @param source$ Observable emitting the base list of entities.
 * @param enrichFn Function that takes an entity and returns an Observable of extra fields.
 * @returns Observable emitting the enriched list of entities.
 */
export function fetchAndConcat<T, R>(
  source$: Observable<T[]>,
  enrichFn: (entity: T) => Observable<R>
): Observable<(T & R)[]> {
  return source$.pipe(
    switchMap((entities) => {
      const enriched$ = entities.map((entity) =>
        enrichFn(entity).pipe(
          map((extra) => ({ ...entity, ...extra })),
          catchError((err) => {
            console.error(
              'Failed to fetch and concat for entity:',
              entity,
              err
            );
            return of({ ...entity } as T & R);
          })
        )
      );
      return forkJoin(enriched$);
    }),
    catchError((err) => {
      console.error('Failed to fetch base entities:', err);
      return of([]);
    })
  );
}
