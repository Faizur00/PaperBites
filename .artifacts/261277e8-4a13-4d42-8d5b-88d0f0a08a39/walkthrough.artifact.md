# Walkthrough - Retrofit Client Singleton

I have successfully implemented the `NetworkModule` singleton, which provides a configured `OpenAlexApi` instance for network operations.

## Changes Made

### Network Layer

#### [NetworkModule.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/network/NetworkModule.kt)

- Created a singleton `object NetworkModule`.
- Configured a `Json` instance with `ignoreUnknownKeys = true` to ensure the app doesn't crash when the OpenAlex API returns unexpected fields.
- Initialized `Retrofit` with the base URL `https://api.openalex.org/` and the `kotlinx.serialization` converter factory.
- Exposed `openAlexApi` as a lazy-initialized singleton.

```kotlin
object NetworkModule {
    private const val BASE_URL = "https://api.openalex.org/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val openAlexApi: OpenAlexApi by lazy {
        retrofit.create(OpenAlexApi::class.java)
    }
}
```

## Verification Results

### Automated Tests
- Ran `:app:assembleDebug` and the build finished successfully, confirming that the new module and its dependencies (Retrofit, OkHttp, Kotlinx Serialization) are correctly integrated.

> [!TIP]
> `NetworkModule.openAlexApi` can now be used throughout the app to perform network requests. For example, it can be injected or accessed in Repositories to fetch research papers.
