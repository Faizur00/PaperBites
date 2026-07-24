# Increase Paging Buffer Size

The user wants to increase the paging buffer size to 50 and update related parameters like refresh size. This involves modifying both the `PagingConfig` in the repository and the fetching logic in the `RemoteMediator`.

## Proposed Changes

### [Component Name] Data Layer

#### [MODIFY] [PaperRepository.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/data/database/Repository/PaperRepository.kt)
- Update `PagingConfig` to set `pageSize = 50`.
- Update `prefetchDistance` to a larger value (e.g., 20) to maintain a healthy buffer.
- Explicitly set `initialLoadSize = 50` (or leave it as default if 150 is desired, but user specifically asked for "buffer to 50" which might imply they want 50 items loaded at once). I'll set `initialLoadSize` to 50 to match the "refresh size" requirement.

#### [MODIFY] [PaperRemoteMediator.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/network/PaperRemoteMediator.kt)
- Update the threshold check for `unseenCount()`. Currently, it's `10`. I'll increase it to `25` or `50` to trigger network fetches earlier, ensuring the buffer stays around 50.
- Update `api.getWorks(perPage = 25, ...)` to `perPage = 50` to match the new buffer size.

## Verification Plan

### Manual Verification
- Deploy the app and monitor the paging behavior.
- Use Logcat to verify that `PaperRemoteMediator` fetches 50 items when the threshold is reached.
- Check that the list loads more items smoothly and with the new larger batch size.
