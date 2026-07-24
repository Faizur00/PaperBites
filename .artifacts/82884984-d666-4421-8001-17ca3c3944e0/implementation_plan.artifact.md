# Implementation Plan - Add Jetpack DataStore

This plan outlines the steps to integrate Jetpack Preferences DataStore into the PaperBytes project for managing user preferences.

## User Review Required

> [!IMPORTANT]
> I am implementing **Preferences DataStore** as it is the most common and straightforward way to store key-value pairs. If you specifically need **Proto DataStore**, please let me know.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/gradle/libs.versions.toml)
- Add `datastore = "1.2.1"` to `[versions]`.
- Add `androidx-datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastore" }` to `[libraries]`.

#### [MODIFY] [build.gradle.kts](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/build.gradle.kts)
- Add `implementation(libs.androidx.datastore.preferences)` to `dependencies`.

### DataStore Implementation

#### [NEW] [UserPreferencesRepository.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/datastore/UserPreferencesRepository.kt)
- Create a repository class to manage DataStore.
- Implement basic functionality:
    - Define a `DataStore` instance using the `preferencesDataStore` delegate.
    - Create keys for preferences (e.g., `IS_DARK_MODE`).
    - Provide a `Flow` to read preferences.
    - Provide functions to update preferences.

## Verification Plan

### Automated Tests
- I will run a Gradle sync to ensure dependencies are correctly added.
- I can create a simple unit test for `UserPreferencesRepository` if desired, but for now, I will focus on the implementation and a build check.

### Manual Verification
- Verify that the app still builds and runs correctly.
- (Optional) I can modify `MainActivity` or a ViewModel to demonstrate reading/writing to DataStore if requested.
