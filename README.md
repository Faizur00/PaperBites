# PaperBites

PaperBites is a modern Android application designed for consuming bite-sized Research Paper.

<!--suppress HtmlDeprecatedAttribute -->
<p align="center">
  <img src="./app/src/main/assets/png/PaperBitesExample_1.jpeg" width="35%" alt="PaperBites Screenshot 1">
  <img src="./app/src/main/assets/png/PaperBitesExample_2.jpeg" width="35%" alt="PaperBites Screenshot 2">
</p>


## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Repository pattern
- **Local Storage**: Room Database
- **Network**: Retrofit with OpenAlex API
- **Paging**: Paging 3 for efficient list loading
- **Dependency Injection**: Manual injection (AppContainer pattern)
- **Asynchronous Work**: Kotlin Coroutines & Flow

## TODO

- [ ] Implement advances search for articles.
- [ ] Add the bottom sheet thing for full abstract read
- [x] Add refresh reloader that shuffle current paper buffer
- [x] Add util for reading full paper on the browser
- [x] Fix latex rendering issues
- [x] Implement Saved and Bookmark
- [x] Implement Read Full Paper for bookmark
- [ ] Add quick share link/doi for the paper
- [ ] Further refine dark mode and add custom color themes.
- [ ] Optimize layout
- [ ] Create a fucking logo m8

## Bugs
- [x] the refresher leading the content insertion from last session when app opened
- [x] glitch happening when bookmarking a paper outside of initial load buffer where page jump to a unexpected page
- [x] paper chance to re-appearing unexpectedly high
