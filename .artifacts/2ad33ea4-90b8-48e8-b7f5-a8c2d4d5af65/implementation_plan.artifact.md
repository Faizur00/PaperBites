# Implementation Plan - PaperRepository

This plan outlines the implementation of `PaperRepository` to bridge the `PaperDao` and the rest of the application.

## Proposed Changes

### Database Layer

#### [MODIFY] [PaperRepository.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/database/Repository/PaperRepository.kt)
Implement the repository methods that wrap `PaperDao` functionality.

- `insertAll(papers: List<PaperEntity>)`: Inserts a list of papers.
- `pagingSource()`: Provides a PagingSource for all unseen papers.
- `unseenCount()`: Returns the total count of unseen papers.
- `pagingSourceByField(field: String)`: Provides a PagingSource for unseen papers in a specific field.
- `unseenCountByField(field: String)`: Returns the count of unseen papers in a specific field.
- `markServed(ids: List<String>, now: Long)`: Marks papers as served.
- `setBookmarked(id: String, saved: Boolean)`: Updates the bookmark status of a paper.

## Verification Plan

### Automated Tests
- I will check if the project has existing tests for repositories or DAOs and suggest adding one if necessary.
- Run `gradle_build` to ensure the code compiles.

### Manual Verification
- Code review of the implemented methods to ensure they correctly delegate to `PaperDao`.
