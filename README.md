# Food Rescue

An Android app that connects people with surplus food (restaurants, bakeries, individuals) to nearby people who can pick it up before it goes to waste, reducing food waste and improving access to free/cheap food.

## Current functionality

- Browse a feed of active food listings — "Ending soon" highlights plus a "Near you" list, loaded from a remote REST API with error and retry states
- Filter the feed by category (bakery, meals, produce, drinks, dairy)
- View full details of a single listing
- Favourite listings (kept for the current session)
- Authentication — account type choice, log in, user sign-up, business sign-up, and password reset, backed by Firebase Authentication; profiles are stored in Firestore
- Empty state when no listings match
- Location-aware feed — see below

### Location (proximity feature)

The feed is proximity based, so on first launch the app asks for the coarse location permission. Every outcome is handled gracefully — the feed keeps working in all of them:

- **Allowed** — the feed header shows the device's real location: city/area resolved via the built-in Geocoder, falling back to raw coordinates
- **Denied** — a banner explains that all listings are still shown, with an **Enable** button that re-opens the dialog; after "Don't ask again" the button opens the app's system settings page instead
- **Device location services off** — a banner with a **Turn on** button that opens the device's location settings; once enabled, the header updates automatically
- **Permission granted but no fix** (or a 5-second timeout) — a banner with a **Retry** button

## Permissions

| Permission | Why | Behaviour |
|---|---|---|
| `INTERNET` | Loads the feed from the remote API | Granted automatically at install |
| `ACCESS_COARSE_LOCATION` | Shows the device's real location in the feed header | Runtime permission, requested once on first launch; denial never blocks the feed — see [Location](#location-proximity-feature) |

## Tech stack

- Kotlin, Jetpack Compose + Material 3
- MVVM with unidirectional state (`StateFlow` UI state per screen)
- Hilt for dependency injection
- Retrofit + Gson for the network layer
- Firebase Authentication (email/password) + Cloud Firestore (user profiles)
- Navigation Compose
- Play Services Location (fused provider) + framework Geocoder
- Kotlin coroutines + Flow

## Project structure

```
app/src/main/java/com/yertaypert/foodrescue/
├── MainActivity.kt                 # Entry point (single Activity)
├── FoodRescueApplication.kt        # Hilt application
├── data/
│   ├── local/                      # Sample data
│   ├── location/                   # Fused-location provider implementation
│   ├── remote/                     # Retrofit API, DTOs, mapper
│   └── repository/                 # Repository implementations
├── di/                             # Hilt modules (network, auth, Firestore, repository bindings)
├── domain/
│   ├── location/                   # LocationProvider interface + LocationOutcome
│   ├── model/                      # Listing, Category, LocationData, UserProfile
│   └── repository/                 # Repository interfaces
└── presentation/
    ├── AppNavHost.kt               # Navigation graph
    ├── components/                 # Reusable composables (cards, chips, banners, ...)
    ├── screens/                    # Feed, Detail, Login, Sign-up + ViewModels / UI states
    ├── util/                       # Shared validation + auth error messages
    └── theme/                      # Color, typography, spacing
```

## Architecture (MVVM)

Diagram: `design/MVVM architecture.png`

UI (Compose) renders purely from ViewModel state (`StateFlow`) → ViewModels talk to repository interfaces → implementations in the data layer wrap the remote API, Firebase Auth/Firestore, and the location provider. Hilt binds interfaces to implementations in `di/`. ViewModels survive configuration changes, so form input, submit state, category filter, favourites, and location status are not lost on rotation.

## Firebase setup

The project is already linked to the `food-rescue-75ada` Firebase project (`app/google-services.json`). One-time console steps to make auth work in a fresh project:

1. **Authentication → Sign-in method → Email/Password → Enable**
2. **Firestore Database → Create database → Native mode**, region closest to you
3. Publish `firestore.rules` — paste it in **Firestore → Rules**, or deploy with the Firebase CLI:

```bash
npm install -g firebase-tools
firebase login
firebase deploy --only firestore:rules
```

Every sign-up writes a `users/{uid}` document with `email`, `displayName`, `accountType` (`user` / `business`), `businessType`, `address`, and `createdAt`. The rules only let a signed-in user read and write their own document. Password reset uses Firebase's hosted email — no extra setup.

## Build & run

**Requirements:** Android Studio (recent stable version), JDK 17+, an emulator or physical device running Android 8.0 (API 26) or higher.

1. Clone the repo:

```
git clone https://github.com/yertaypert/food-rescue.git
```

2. Open the project folder in Android Studio and let Gradle sync automatically.
3. Select a device/emulator from the device dropdown.
4. Click **Run** (▶) or use:

```
./gradlew installDebug
```
