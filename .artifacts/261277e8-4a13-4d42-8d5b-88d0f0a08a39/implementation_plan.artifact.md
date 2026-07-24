# Implementation Plan - Retrofit Client Singleton

This plan details the creation of a singleton network module to handle API calls to OpenAlex using Retrofit and Kotlinx Serialization.

## Proposed Changes

### Network Layer

#### [NEW] [NetworkModule.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/network/NetworkModule.kt)

- Create a singleton `object NetworkModule`.
- Configure `Json` instance with `ignoreUnknownKeys = true` to handle extra fields in OpenAlex API responses.
- Initialize `Retrofit` with:
    - Base URL: `https://api.openalex.org/`
    - `MediaType` set to `application/json`.
    - `kotlinx.serialization` converter factory.
- Expose `openAlexApi` property using `retrofit.create(OpenAlexApi::class.java)`.

## Verification Plan

### Automated Tests
- I will run `gradle_build(":app:assembleDebug")` to ensure the new module compiles correctly and the dependencies are correctly linked.

### Manual Verification
- Verify that `NetworkModule` is accessible from other parts of the app (e.g., in a future Repository implementation).
