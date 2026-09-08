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

## Build & run

**Requirements:** Android Studio (recent stable version), JDK 17+, an emulator or physical device running Android 8.0 (API 26) or higher.

1. Clone the repo:

`git clone https://github.com/yertaypert/food-rescue.git`

2. Open the project folder in Android Studio and let Gradle sync automatically.
3. Select a device/emulator from the device dropdown.
4. Click **Run** (▶) or use:
`./gradlew installDebug`