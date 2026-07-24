# Isolated Network Test Walkthrough

I have implemented the temporary network test as requested. This test will prove that the app can connect to the OpenAlex API independently of any other components like Room or Paging.

## Changes Made

### Network API
Modified [OpenAlexApi.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/network/OpenAlexApi.kt) to include `sample` and `seed` query parameters. This allows us to request a random sample of papers from OpenAlex.

### Connectivity Test
Injected a network call within [MainActivity.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/MainActivity.kt). Upon app launch, it will:
1. Call `NetworkModule.openAlexApi.getWorks(sample = 5, seed = 1234)`.
2. Log the titles of the 5 papers received.
3. Handle and log any exceptions that might occur during the network request.

## Verification Instructions

To verify the network connection:

1. **Deploy the app** to a device or emulator.
2. Open the **Logcat** tool window in Android Studio.
3. Apply a filter for the tag `NETWORK_TEST`:
   - Filter string: `tag:NETWORK_TEST`
4. Look for the following output (paper titles will vary but should be 5):
   ```
   D/NETWORK_TEST: Successfully fetched 5 works
   D/NETWORK_TEST: Paper Title: [Real Paper Title 1]
   D/NETWORK_TEST: Paper Title: [Real Paper Title 2]
   ...
   ```

> [!NOTE]
> Once you confirm that the paper titles are appearing in Logcat, please let me know. I will then remove this temporary code to keep the project clean.
