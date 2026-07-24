# Database Migration to add `subfield` to `PaperEntity`

This plan outlines the steps to add a new `subfield` column to the `PaperEntity` and perform a Room database migration from version 2 to 3.

## User Review Required

> [!IMPORTANT]
> The migration will add a nullable `subfield` column. Existing data will have `NULL` for this column until re-seeded or updated.

## Proposed Changes

### Database Component

#### [MODIFY] [PaperEntity.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/Entity/PaperEntity.kt)
- Add `val subfield: String? = null` to the `PaperEntity` data class.
- Since it's annotated with `@Serializable` and used in `PaperRepository` with `ignoreUnknownKeys = true`, it will automatically pick up the new field from `PapersData.json` during the next seeding (if it ever runs again, or if the user clears data).

#### [MODIFY] [AppDatabase.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/AppDatabase.kt)
- Increment database version from 2 to 3.
- Define `MIGRATION_2_3`:
  ```kotlin
  val MIGRATION_2_3 = object : Migration(2, 3) {
      override fun migrate(database: SupportSQLiteDatabase) {
          database.execSQL("ALTER TABLE papers ADD COLUMN subfield TEXT")
      }
  }
  ```
- Add the migration to the Room database builder.

## Verification Plan

### Automated Tests
- Run a build to ensure no compilation errors.
- Ideally, add a Room migration test, but since I cannot easily run instrumented tests here, I will rely on manual verification by the user and ensuring the SQL is correct.

### Manual Verification
- Deploy the app.
- Check if the app starts without crashing (verifies migration executed successfully).
- If the user has already seeded the database, they might need to clear data to see the new `subfield` data populated from JSON, or we could trigger a re-seed (but the current `seedIfNeeded` logic prevents it if already seeded).
