# Resolve All Warning Issues and Verify Stability

Resolve all static analysis/lint warnings across the project (unused imports, unused classes/properties/functions, missing trailing commas, package naming conventions, etc.), and ensure nothing breaks via a clean build and test check.

## User Review Required

> [!IMPORTANT]
> This is a comprehensive cleanup of warning messages across all Kotlin source files. No functional behavior of the app will be modified.

## Open Questions

None.

## Proposed Changes

### [Component Name]

#### [MODIFY] [MainActivity.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/MainActivity.kt)
- Remove unused import `AppDatabase`.

#### [MODIFY] [MainFeedViewModel.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/ui/mainfeed/MainFeedViewModel.kt)
- Add missing trailing comma.

#### [MODIFY] [MainFeedScreen.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/ui/mainfeed/MainFeedScreen.kt)
- Remove unused import `remember`.
- Add trailing commas, format pagerState lambda properly, add clarifying parentheses.

#### [MODIFY] [Article.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/ui/mainfeed/components/Article.kt)
- Add trailing comma, name boolean literal parameter, move lambda out of parentheses.

#### [MODIFY] [AppDatabase.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/AppDatabase.kt)
- Add trailing comma, update deprecated `fallbackToDestructiveMigration()` or handle appropriately.

#### [MODIFY] [BookmarkDao.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/Dao/BookmarkDao.kt) & [PaperDao.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/Dao/PaperDao.kt)
- (Note: Package directory/name casing warnings for `Dao`, `Entity`, `Repository` packages. We can either leave package names or refactor package structure if appropriate, but package renames affect FQN across the database layer. Let's fix safe warnings first or suppress/fix package name casing if needed.)

#### [MODIFY] [BookmarkRepository.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/Repository/BookmarkRepository.kt) & [PaperRepository.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/Repository/PaperRepository.kt)
- Clean up unused classes/properties/functions or keep if required by architecture. Wait, unused repository/functions should be cleaned up or checked if they are part of future implementation. Let's clean up unused imports and syntax warnings.

#### [MODIFY] UI Components and other files
- Clean up formatting warnings, trailing commas, clarifying parentheses across components.

## Verification Plan

### Automated Tests
- Run `gradle_build("app:assembleDebug")` to ensure compilation succeeds with zero errors.

### Manual Verification
- Project builds successfully and runs without runtime issues.
