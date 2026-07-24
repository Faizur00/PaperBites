# Walkthrough - Language Support & Database Seeding

I have implemented the infrastructure for language-based filtering and enabled the database seeding process. The app is now ready to handle the new `language` field in `PapersData.json`.

## Changes Made

### Data Layer Updates
- **[PaperEntity.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/Entity/PaperEntity.kt)**: Added `language: String?` field and indexed it for efficient querying.
- **[PaperDao.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/Dao/PaperDao.kt)**: Added `pagingSourceByLanguage` and `unseenCountByLanguage` methods.
- **[AppDatabase.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/AppDatabase.kt)**: Bumped version to `2` and enabled `fallbackToDestructiveMigration()` to automatically handle the schema change.

### Repository & Seeding logic
- **[PaperRepository.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/Repository/PaperRepository.kt)**:
    - Configured a **resilient JSON decoder** that ignores unknown keys and handles missing fields gracefully.
    - Exposed new language filtering methods to the repository.
- **[MainActivity.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/MainActivity.kt)**: Added a `LaunchedEffect` to trigger `seedIfNeeded()` on startup. This will populate the database the first time the app runs.

### UI Sync
- **[tempData.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/tempData.kt)**: Updated `ArticleData` to include the `language` field (defaulting to "en") to maintain consistency with the database model.

## How to Verify

1.  **Update JSON**: Add the `language` field to your `PapersData.json` file.
2.  **Run App**: Launch the app on a device or emulator.
3.  **Check Logs**: Look for "SEED" tags in Logcat. You should see "Insert complete" once the 10,000 rows are processed.
4.  **Database Inspection**: Use the **App Inspection** tool in Android Studio to verify that the `papers` table now contains a `language` column with your data.

> [!TIP]
> Since the `seedIfNeeded()` check uses DataStore, the seeding will only run once. If you update the JSON file later and want to re-seed, you can either clear the app's storage or increment the database version again.
