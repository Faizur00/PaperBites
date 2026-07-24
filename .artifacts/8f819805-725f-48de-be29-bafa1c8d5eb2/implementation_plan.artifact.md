# LaTeX Rendering Support Plan

Implement inline LaTeX rendering for paper abstracts and titles using `io.github.huarangmeng:latex-renderer` and Compose `InlineTextContent`.

## User Review Required

> [!IMPORTANT]
> The implementation uses `io.github.huarangmeng:latex-renderer`, which is a contemporary (2026) Compose-native library. It supports pre-measurement required for `InlineTextContent`.

> [!NOTE]
> Caching of rendered formula dimensions is implemented using an `LruCache` to ensure smooth scrolling in the feed.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/gradle/libs.versions.toml)
- Add `latex` version.
- Add `latex-renderer` library.

#### [MODIFY] [build.gradle.kts](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/build.gradle.kts)
- Add `latex-renderer` dependency.

---

### Core Logic

#### [MODIFY] [TextFormatting.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/ui/common/TextFormatting.kt)
- Update `parseFormattedText` to detect `$ ... $` and `\( ... \)` delimiters.
- Update it to return `FormattedText` instead of `AnnotatedString`.
- `FormattedText` will contain the `AnnotatedString` (with `appendInlineContent` calls) and a map of LaTeX formulas.

#### [NEW] [LatexSupport.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/ui/common/LatexSupport.kt)
- Define `FormattedText` data class.
- Implement `LatexCache` (singleton with `LruCache`).
- Implement `rememberFormattedText` composable that:
    1. Parses the raw text.
    2. Pre-measures LaTeX formulas (using cache).
    3. Builds `Map<String, InlineTextContent>`.
    4. Returns a helper object containing `AnnotatedString` and `inlineContent`.

---

### UI Components

#### [MODIFY] [Article.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/ui/mainfeed/components/Article.kt)
- Update `ArticleTitle` and `ArticleAbstract` to use `rememberFormattedText`.
- Pass `inlineContent` to the `Text` composable.

#### [MODIFY] [BookmarkCard.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/main/java/com/example/paperbytes/ui/bookmarks/components/BookmarkCard.kt)
- Update to use `rememberFormattedText` and pass `inlineContent` to `Text`.

---

### Tests

#### [MODIFY] [TextFormattingTest.kt](file:///C:/Users/eonz/AndroidStudioProjects/PaperBytes/app/src/test/java/com/example/paperbytes/ui/common/TextFormattingTest.kt)
- Update existing tests to handle the change in return type.
- Add new test cases for `$ ... $` and `\( ... \)` delimiters.

## Verification Plan

### Automated Tests
- Run `TextFormattingTest` to verify delimiter detection and placeholder insertion.

### Manual Verification
- Deploy to emulator/device.
- Verify that abstracts containing LaTeX (e.g., in the `ArticlePreview` or real data) render correctly inline.
- Verify scrolling performance is not impacted (checking cache efficiency).
- Test fallback behavior by providing malformed LaTeX.
