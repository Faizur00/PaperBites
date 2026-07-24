# Walkthrough - Phase 10: ViewModel and UI Wiring

I have connected the real paging pipeline to the UI, enabling automatic network fetching as you scroll and persistent seen/unseen tracking.

## Changes Made

### Dependency Injection & Infrastructure
- Created [AppContainer.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/AppContainer.kt) to manage singleton instances of the database, API, and repository.
- Created [PaperBytesApplication.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/PaperBytesApplication.kt) and registered it in the [AndroidManifest.xml](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/AndroidManifest.xml).
- Implemented [AppViewModelProvider.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/ui/viewmodel/AppViewModelProvider.kt) to handle ViewModel creation with dependencies.

### Data Stream & Business Logic
- Implemented [MainFeedViewModel.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/ui/mainfeed/MainFeedViewModel.kt):
    - Exposes `pagedPapers` using `.cachedIn(viewModelScope)` to survive rotations.
    - Added `markAsServed(ids)` to update Room when the user scrolls past a paper.
    - Added `toggleBookmark(paper)` to persist bookmark status.

### UI Integration
- Updated [Article.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/ui/mainfeed/components/Article.kt) to display data from the real `PaperEntity` instead of mock objects.
- Refactored [MainFeedScreen.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/ui/mainfeed/MainFeedScreen.kt):
    - Replaced the static sample list with `LazyPagingItems`.
    - **Scroll Tracking**: Added a `LaunchedEffect` that monitors the current page. As you scroll, it collects IDs of all preceding papers and marks them as "served" in the database.
    - Connected the "Save" button to the `ViewModel` for real persistence.

## Verification Results

### Automated Tests
- Successfully ran `:app:assembleDebug`.

### Expected Behavior
1. **Fresh Feed**: On first launch, the feed is populated by the `PapersData.json` seed.
2. **Seamless Refill**: As you scroll through the seeded papers, the `RemoteMediator` detects when you're running low on unseen content and silently fetches a new batch from the OpenAlex API.
3. **Persistence**: Papers you've scrolled past are marked as `served = 1`. If you restart the app, they will no longer appear in the "For You" feed.
