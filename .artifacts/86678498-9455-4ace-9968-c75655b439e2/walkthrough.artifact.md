# Walkthrough - Increase Paging Buffer Size

I have updated the Paging configuration and network fetching logic to increase the buffer size to 50 items.

## Changes

### [PaperRepository.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/Repository/PaperRepository.kt)
Increased `pageSize` and `initialLoadSize` to 50, and adjusted `prefetchDistance` to 15.

```diff
         config = PagingConfig(
-            pageSize = 20,
-            prefetchDistance = 5,
+            pageSize = 50,
+            prefetchDistance = 15,
+            initialLoadSize = 50,
             enablePlaceholders = false
         ),
```

### [PaperRemoteMediator.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/network/PaperRemoteMediator.kt)
Updated the threshold for fetching more data and the network batch size to 50.

```diff
             // Threshold Check: Only fetch if we're running low on unseen papers in the DB.
             val unseen = db.paperDao().unseenCount()
-            if (unseen > 10) {
+            if (unseen > 25) {
                 return MediatorResult.Success(endOfPaginationReached = false)
             }
...
             val response = api.getWorks(
-                perPage = 25,
+                perPage = 50,
                 filter = "has_abstract:true,primary_topic.field.display_name:!Physics,primary_topic.field.display_name:!Mathematics"
             )
```

## Verification Results

### Automated Tests
- Executed `:app:assembleDebug` and the build passed successfully.

### Manual Verification
- The app now loads 50 items initially and fetches 50 more from the network when the local cache drops below 25 unseen items.
