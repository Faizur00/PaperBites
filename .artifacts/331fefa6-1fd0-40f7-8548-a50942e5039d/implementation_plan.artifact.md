# Exclude Physics and Math from Paging

The goal is to exclude papers with the `fieldName` of "Physics" or "Math" from the default paged paper list shown to the user.

## User Review Required

None. This is a direct implementation requirement.

## Open Questions

None.

## Proposed Changes

### [Database Layer]

#### [MODIFY] [PaperDao.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/Dao/PaperDao.kt)
- Update `pagingSource()` to include a `WHERE` clause that filters out `fieldName = 'Physics'` and `fieldName = 'Math'`.
- Update `unseenCount()` to include the same filtering logic.

## Verification Plan

### Automated Tests
- N/A

### Manual Verification
- Deploy the app and navigate to the main feed.
- Ensure no papers with the domain/field of "Physics" or "Math" appear in the list.
- Check that papers from other domains are still visible.
