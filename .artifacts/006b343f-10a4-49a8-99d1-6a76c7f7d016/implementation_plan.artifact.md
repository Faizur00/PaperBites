# Implementation Plan - Phase 10: ViewModel and UI Wiring

This phase focuses on connecting the `PaperRepository`'s paged feed to the UI using a `ViewModel` and updating the `MainFeedScreen` to handle the real data and scroll-to-serve logic.

## Proposed Changes

### Infrastructure

#### [NEW] [PaperBytesApplication.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/PaperBytesApplication.kt)
- Create a custom `Application` class to hold the `AppContainer`.
- Initialize `AppContainer` in `onCreate()`.

#### [NEW] [AppContainer.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/AppContainer.kt)
- Create an `AppContainer` interface and implementation to manage dependencies like `AppDatabase`, `OpenAlexApi`, and `PaperRepository`.

#### [NEW] [AppViewModelProvider.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/ui/viewmodel/AppViewModelProvider.kt)
- Implement a `ViewModelProvider.Factory` to inject dependencies into ViewModels.

### ViewModels

#### [NEW] [MainFeedViewModel.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/ui/mainfeed/MainFeedViewModel.kt)
- Expose `pagedPapers` from `PaperRepository` using `cachedIn(viewModelScope)`.
- Implement `markAsServed(ids: List<String>)` to track user progress.
- Implement `toggleBookmark(paper: PaperEntity)` (connecting to `BookmarkDao` or `PaperRepository`).

### UI Components

#### [MODIFY] [Article.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/ui/mainfeed/components/Article.kt)
- Update `Article` composable to accept `PaperEntity` instead of `ArticleData`.
- Map `PaperEntity` fields (title, authorsDisplay, abstract, primaryTopicName, publicationYear) to the UI.

#### [MODIFY] [MainFeedScreen.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/ui/mainfeed/MainFeedScreen.kt)
- Use `MainFeedViewModel` to observe `pagedPapers` as `LazyPagingItems`.
- Update `VerticalPager` to use `LazyPagingItems`.
- Implement a `LaunchedEffect` with `snapshotFlow` to monitor `pagerState.currentPage`.
- When the page changes, call `viewModel.markAsServed()` for all items the user has scrolled past.
- Handle bookmark toggling via the `ViewModel`.

### Configuration

#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/AndroidManifest.xml)
- Register `PaperBytesApplication` in the `<application>` tag.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/MainActivity.kt)
- Remove manual repository instantiation.
- `MainFeedScreen` will now internally obtain its `ViewModel`.

## Key Technical Decisions

- **Caching**: `.cachedIn(viewModelScope)` is crucial to prevent reload on configuration changes (like rotation).
- **Scroll Tracking**: Using `snapshotFlow` on `pagerState.currentPage` allows us to reactively mark papers as served as soon as they are "passed" by the user.
- **Dependency Injection**: A simple manual `AppContainer` approach is used to keep things lightweight while still following good architectural practices.

## Verification Plan

### Automated Tests
- Run `gradle_build` to ensure the project compiles.

### Manual Verification
- Deploy to a device/emulator.
- Verify that the feed loads real data from OpenAlex (or the seeded data initially).
- Scroll through a few papers, restart the app, and verify that the papers you scrolled past are no longer in the feed (since they are marked as `served`).
