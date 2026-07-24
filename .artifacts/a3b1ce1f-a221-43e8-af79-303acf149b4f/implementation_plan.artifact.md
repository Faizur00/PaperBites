# Implement Search Filter Persistence and Functionality

This plan covers making the "APPLY TUNINGS" and "RESET" buttons functional by persisting filter preferences and updating the data flow to respect these filters.

## User Review Required

> [!IMPORTANT]
> - **Feed Reset**: Applying new filters will reset the current feed position to the top to show the most relevant results immediately.
> - **Subfield Matching**: We will match subfields by their display names (e.g., "Artificial Intelligence") as provided in the topic selection, which corresponds to the `subfield` field in our `PaperEntity`.

## Proposed Changes

### [Data & Persistence]

#### [MODIFY] [UserPreferencesRepository.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbites/datastore/UserPreferencesRepository.kt)
- Add `FILTER_FIELD_ID`, `FILTER_SUBFIELD_IDS`, `FILTER_FROM_YEAR`, and `FILTER_TO_YEAR` keys.
- Create a `FilterSettings` data class.
- Add `filterSettingsFlow` to observe all filter settings.
- Add `updateFilterSettings(settings: FilterSettings)` and `resetFilterSettings()`.

#### [MODIFY] [PaperEntity.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbites/data/database/Entity/PaperEntity.kt)
- Ensure indices are optimized for the new filtering queries (already looks decent, but will verify).

#### [MODIFY] [PaperDao.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbites/data/database/Dao/PaperDao.kt)
- Add a new `@Query` that supports filtering by `fieldName`, `subfield`, and `publicationYear`.

### [Network]

#### [MODIFY] [Mappers.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbites/network/Mappers.kt)
- Update `toPaperEntity()` to correctly map `subfield = this.primaryTopic?.subfield?.displayName`.

#### [MODIFY] [PaperRemoteMediator.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbites/network/PaperRemoteMediator.kt)
- Update `load()` to accept `FilterSettings`.
- Construct the OpenAlex API `filter` query string dynamically based on these settings.

### [Logic & UI]

#### [MODIFY] [PaperRepository.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbites/data/database/Repository/PaperRepository.kt)
- Update `pagedPapers` to react to `FilterSettings` changes.
- Ensure the `RemoteMediator` receives the current filters.

#### [MODIFY] [MainFeedViewModel.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbites/ui/mainfeed/MainFeedViewModel.kt)
- Add `filterSettings` state flow.
- Add `applyFilters(...)` and `resetFilters()` methods.

#### [MODIFY] [DrawerContent.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbites/ui/mainfeed/components/DrawerContent.kt)
- Pass current filter settings from `MainFeedScreen` to `DrawerContent`.
- Implement `onApply` and `onReset` callbacks.
- Initialize drawer state from the passed-in settings.

## Verification Plan

### Automated Tests
- Run `app:assembleDebug` to ensure compilation.
- (Optional) Unit test for `FilterSettings` persistence.

### Manual Verification
- Open the drawer, change years and subfields, click "APPLY TUNINGS". Verify the feed updates.
- Click "RESET" in the drawer and verify settings return to defaults.
- Verify that scrolling triggers the `RemoteMediator` with the correct network filters (checking logcat).
