# Support for Language Filtering in Papers

The goal is to update the application to support a new `language` field in the paper data, enabling future filtering capabilities. This involves changes from the data layer (Room database) up to the repository, and ensuring the initial seeding process works correctly since the database hasn't been seeded yet.

## User Review Required

> [!IMPORTANT]
> The database has not been seeded yet. I will update `PaperRepository` to handle the new `language` field during the seeding process.

> [!NOTE]
> I will configure the JSON decoder to be more resilient (ignoring unknown keys and handling missing values) to ensure `seedIfNeeded()` doesn't crash if the JSON structure varies slightly during your edits.

> [!TIP]
> Since you haven't seeded the DB yet, we don't need to worry about destructive migration side effects for now, but I'll still set it up to be safe for future schema changes.

## Proposed Changes

### Data Layer

#### [MODIFY] [PaperEntity.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/Entity/PaperEntity.kt)
- Add `val language: String? = null` to the `PaperEntity` data class.
- Add an index for the `language` column.

#### [MODIFY] [PaperDao.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/Dao/PaperDao.kt)
- Add `pagingSourceByLanguage(language: String)` query.
- Add `unseenCountByLanguage(language: String)` query.

#### [MODIFY] [AppDatabase.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/AppDatabase.kt)
- Increment version to `2`.
- Add `fallbackToDestructiveMigration()`.

### Repository Layer

#### [MODIFY] [PaperRepository.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/Repository/PaperRepository.kt)
- Configure a local `Json` instance with `ignoreUnknownKeys = true` and `explicitNulls = false`.
- Update `seedIfNeeded()` to use this resilient `Json` instance.
- Expose the new language-based DAO methods.

### UI & Bridge

#### [MODIFY] [tempData.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/tempData.kt)
- Add `language` to `ArticleData` so it stays in sync with `PaperEntity`.

## Verification Plan

### Automated Tests
- Verify project compilation.
- I will write a small "scratch" script to verify the JSON decoding logic with a sample language field.

### Manual Verification
- Once you update `PapersData.json` and run the app, the `seedIfNeeded()` method (once called) will populate the database with the new language data.
