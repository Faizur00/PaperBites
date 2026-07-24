# Isolated Network Test Plan

This plan aims to verify the network connection to the OpenAlex API by fetching a sample of 5 works and logging their titles.

## User Review Required

> [!IMPORTANT]
> The changes to `MainActivity.kt` and `OpenAlexApi.kt` are temporary and should be removed after the test is successful.

## Proposed Changes

### Network Component

#### [MODIFY] [OpenAlexApi.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/network/OpenAlexApi.kt)
- Add `sample` and `seed` parameters to the `getWorks` function to support random sampling as requested.

### UI Component

#### [MODIFY] [MainActivity.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/MainActivity.kt)
- Inject a temporary network call in `onCreate` using `lifecycleScope`.
- Log the results (titles of 5 papers) to Logcat with the tag `NETWORK_TEST`.

## Verification Plan

### Manual Verification
1. Build and run the app.
2. Open the Logcat window in Android Studio.
3. Filter by tag: `NETWORK_TEST`.
4. Confirm that 5 real paper titles are printed.
5. Report back the results to proceed to the next phase.
