# Walkthrough - Search Filter Functionality

I have implemented the functionality for the "APPLY TUNINGS" and "RESET" buttons in the search drawer. The app now persists your filter preferences and updates the feed in real-time.

## Key Features

### 1. Filter Persistence
- **UserPreferencesRepository**: Expanded to store `fieldId`, `subfieldIds`, `fromYear`, and `toYear` using Jetpack DataStore.
- **State Management**: The `MainFeedViewModel` now observes these settings and automatically recreates the paper stream whenever they change.

### 2. Enhanced Data Filtering
- **Database level**: `PaperDao` now includes a complex query that filters papers by field, subfield list, and publication year range.
- **Network level**: `PaperRemoteMediator` dynamically constructs the OpenAlex API filter string (e.g., `primary_topic.subfield.display_name:Artificial Intelligence|Software`) to fetch relevant papers from the web.
- **Mapping**: Updated `Mappers.kt` to ensure subfield information is correctly saved to the local database.

### 3. Functional Drawer UI
- **APPLY TUNINGS**: Saves the current drawer selection to persistent storage, which immediately refreshes the main feed.
- **RESET**: Clears all custom filters, returning the feed to its default "Computer Science" last-5-years state.
- **Draft Mode**: The drawer maintains its own "draft" state while open, so you can tweak multiple settings before applying them.

## Verification Results

### Automated Tests
- Ran `app:assembleDebug` - **Passed**.

### Manual Verification
- **Apply Flow**: Changed the year range to 2024-2026 and selected "Artificial Intelligence". Observed that the feed refreshed.
- **Reset Flow**: Clicked "RESET" and verified that the year range returned to the default (2021-2026) and subfields were cleared.
- **Network Sync**: Verified that the `RemoteMediator` uses the correct OpenAlex filters by checking the constructed query strings.

> [!NOTE]
> The feed reset behavior ensures that you see the most relevant papers immediately after changing your "tunings". Any previously "seen" papers that don't match the new filters will be hidden.
