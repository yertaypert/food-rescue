# Food Rescue

An Android app that connects people with surplus food (restaurants, bakeries, individuals) to nearby people who can pick it up before it goes to waste, reducing food waste and improving access to free/cheap food.

## Functionality

1. User can create an account / log in (needed to post or claim listings)
2. User can post a food listing with photo, quantity, and pickup time window
3. User can browse a list of nearby active listings
4. User can view full details of a single listing
5. User can claim a listing, generating a unique pickup code
6. App persists listings and claims locally (so data survives app restart)
7. App shows an empty state when no listings are nearby
8. User can view their own posted listings and their status (active/claimed/expired)
9. User can view a history of items they've claimed
10. App marks a listing as expired automatically once its pickup window passes

## Project structure

```
app/
└── src/
    ├── main/
    │   ├── java/com/yertaypert/foodrescue/
    │   │   ├── MainActivity.kt        # App entry point
    │   │   └── ui/theme/              # Compose theme (color, typography, styling)
    │   ├── res/                       # App resources (icons, strings, themes)
    │   └── AndroidManifest.xml
    ├── androidTest/                   # Instrumented tests (run on device/emulator)
    └── test/                          # Local unit tests (run on JVM)

```

*Note: this reflects the initial Empty Activity scaffold. Screens, navigation, and data layers (per the app's flow chart) will be added as separate packages under `java/com/yertaypert/foodrescue/` as development progresses.*

## Architecture (MVVM - Feed)

### At `design/MVVM architecture.png`


## Build & run

**Requirements:** Android Studio (recent stable version), JDK 17+, an emulator or physical device running Android 8.0 (API 26) or higher.

1. Clone the repo:

`git clone https://github.com/yertaypert/food-rescue.git`

2. Open the project folder in Android Studio and let Gradle sync automatically.
3. Select a device/emulator from the device dropdown.
4. Click **Run** (▶) or use:

`./gradlew installDebug`

## Log on rotation


### Filtered Logcat (`package:mine level:DEBUG tag:MainActivity|FeedViewModel`):

```
// App start
2026-10-13 12:32:24.719 MainActivity D onCreate
2026-10-13 12:32:24.767 MainActivity D onStart
2026-10-13 12:32:24.768 MainActivity D onResume
2026-10-13 12:32:25.136 FeedViewModel D init

// Rotation 1: portrait -> landscape
2026-10-13 12:33:16.271 MainActivity D onPause
2026-10-13 12:33:16.273 MainActivity D onStop
2026-10-13 12:33:16.292 MainActivity D onDestroy
2026-10-13 12:33:16.326 MainActivity D onCreate
2026-10-13 12:33:16.342 MainActivity D onStart
2026-10-13 12:33:16.343 MainActivity D onResume

// Rotation 2: landscape -> portrait
2026-10-13 12:33:18.240 MainActivity D onPause
2026-10-13 12:33:18.243 MainActivity D onStop
2026-10-13 12:33:18.257 MainActivity D onDestroy
2026-10-13 12:33:18.278 MainActivity D onCreate
2026-10-13 12:33:18.292 MainActivity D onStart
2026-10-13 12:33:18.293 MainActivity D onResume
// No second FeedViewModel init, no onCleared
```

### Explanation:

> **_NOTE:_**  When I rotated the device the Activity went through onPause -> onStop -> onDestroy -> onCreate -> onStart -> onResume, 
> so it was fully recreated. The FeedViewModel logged init only once at startup and never logged onCleared during rotation. 
> This is because the ViewModel is kept in the ViewModelStore that survives configuration changes, while the Activity instance is thrown away. 
> That is why search text, category filter and favourites held in FeedUiState did not vanish.
