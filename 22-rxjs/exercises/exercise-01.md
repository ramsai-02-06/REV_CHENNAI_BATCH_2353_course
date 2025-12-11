# Exercise: RxJS Operators and Patterns

## Objective
Master RxJS by implementing common reactive patterns for search, real-time updates, and state management.

## Requirements

### Scenarios to Implement

1. **Type-ahead Search**
   - Debounce user input (300ms)
   - Skip if less than 3 characters
   - Cancel previous requests
   - Handle errors gracefully

2. **Real-time Data Polling**
   - Poll API every 5 seconds
   - Stop when component destroys
   - Retry on failure with exponential backoff

3. **Combining Multiple Streams**
   - Load user and their posts in parallel
   - Wait for both before displaying
   - Handle partial failures

4. **State Management with BehaviorSubject**
   - Centralized store pattern
   - Selectors for derived state
   - Actions for state updates

### RxJS Operators to Use

**Creation:**
- `of`, `from`, `fromEvent`, `interval`, `timer`

**Transformation:**
- `map`, `switchMap`, `mergeMap`, `concatMap`
- `scan`, `reduce`

**Filtering:**
- `filter`, `debounceTime`, `distinctUntilChanged`
- `take`, `takeUntil`, `skip`

**Combination:**
- `combineLatest`, `forkJoin`, `merge`, `zip`

**Error Handling:**
- `catchError`, `retry`, `retryWhen`

**Utility:**
- `tap`, `delay`, `finalize`, `shareReplay`

### Code Examples

```typescript
// Type-ahead search
searchTerm$.pipe(
  debounceTime(300),
  distinctUntilChanged(),
  filter(term => term.length >= 3),
  switchMap(term => this.searchService.search(term)),
  catchError(err => of([]))
)

// Polling with takeUntil
interval(5000).pipe(
  switchMap(() => this.api.getData()),
  takeUntil(this.destroy$),
  retry(3)
)

// Combine streams
forkJoin({
  user: this.userService.getUser(id),
  posts: this.postService.getUserPosts(id)
}).pipe(
  map(({ user, posts }) => ({ ...user, posts }))
)
```

## Expected Implementations

1. Search component with debounced API calls
2. Dashboard with real-time data polling
3. User profile with combined data streams
4. Simple store using BehaviorSubject

## Skills Tested
- RxJS operators understanding
- Reactive programming patterns
- Memory leak prevention
- Error handling strategies
- Stream composition
