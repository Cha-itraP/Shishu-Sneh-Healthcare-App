# 👶 ShishuSneh — Baby Care Companion App

**Android App | Kotlin | Jetpack Compose | Room DB | MVVM | WorkManager | MPAndroidChart**

---

## 📋 Problem Statement / Overview

New parents often struggle to keep track of their newborn's feeding schedules, growth measurements, vaccination timelines, and developmental milestones across multiple apps or paper records. There is a need for a single, offline-first mobile solution that centralises all critical baby care data in a simple, intuitive interface.

ShishuSneh (शिशु स्नेह) is an Android app that addresses this by providing a comprehensive baby care companion — helping new parents log feedings, monitor growth, track vaccines, and celebrate milestones, all in one place without requiring an internet connection.

---
## ✨ Features

* 👶 Baby profile setup with age tracking
* 🍼 Feeding logger (breastfeed & bottle sessions)
* 📊 Growth tracking (Weight, Height, Head circumference)
* 💉 Vaccination schedule with reminders
* ✅ Developmental milestone tracker
* 📈 Feeding & growth charts
* 📖 Parenting guide & nutrition tips
* 🔔 Background notifications (WorkManager)
* 📴 Fully offline — no internet required

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose + Material3 |
| **Architecture** | MVVM + Clean Architecture |
| **Database** | Room (SQLite) with KSP code generation |
| **State Management** | StateFlow + ViewModel |
| **Navigation** | Navigation Compose (animated transitions) |
| **Background Tasks** | WorkManager (vaccination reminders) |
| **Charts** | MPAndroidChart 3.1.0 (bar + line charts) |
| **Persistence** | DataStore Preferences (auth/user data) |
| **Async** | Kotlin Coroutines + Flow |
| **Build System** | Gradle (Kotlin DSL) |
| **Min / Target SDK** | API 26 / API 35 |

---


## 📁 Project Structure

```
ShishuSneh/
├── app/
│   ├── build.gradle.kts                    ← App-level dependencies
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/shishusneh/app/
│           ├── MainActivity.kt             ← App entry point + Root navigation
│           ├── Worker/
│           │   └── VaccinationReminderWorker.kt  ← Background reminders
│           ├── data/
│           │   ├── model/
│           │   │   ├── Models.kt           ← Domain models (Baby, Vaccine, Milestone, etc.)
│           │   │   └── FeedingModels.kt    ← Feeding-specific models
│           │   ├── auth/
│           │   │   └── UserPreferences.kt  ← DataStore auth persistence
│           │   ├── db/
│           │   │   ├── AppDatabase.kt      ← Room DB singleton + DAOs
│           │   │   └── ShishuDbRepository.kt
│           │   ├── ShishuRepository.kt     ← In-memory data source
│           │   └── FeedingRepository.kt    ← Feeding log data source
│           ├── viewmodel/
│           │   ├── ShishuViewModel.kt      ← Shared ViewModel + UI State
│           │   └── AuthViewModel.kt        ← Auth flow ViewModel
│           └── ui/
│               ├── theme/
│               │   └── Theme.kt            ← Material3 theming
│               ├── components/
│               │   ├── SharedComponents.kt ← Reusable Compose components
│               │   └── FeedingCharts.kt    ← Bar + Line chart components
│               └── screens/
│                   ├── LoginScreen.kt
│                   ├── SignupScreen.kt
│                   ├── HomeScreen.kt       ← Dashboard with today's summary
│                   ├── LogScreen.kt        ← Feeding log + history
│                   ├── MilestonesScreen.kt ← Development milestones tracker
│                   ├── HealthScreen.kt     ← Vaccination schedule + growth
│                   ├── GuideScreen.kt      ← Parenting guide articles
│                   ├── NutritionTipsScreen.kt
│                   ├── ProfilePanel.kt
│                   ├── AddFeedingSheet.kt  ← Bottom sheet for logging feeds
│                   └── GrowthDetailPanel.kt
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 🚀 Steps to Run in Android Studio

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK with API 26+ installed
- An Android emulator or physical device

---

### Step 1: Open the Project

1. Launch Android Studio
2. Click **File → Open**
3. Navigate to and select the `ShishuSneh/` folder
4. Click **OK**
5. Wait for Gradle sync to complete (first time may take 3–5 minutes)

---

### Step 2: Add JitPack Repository (for MPAndroidChart)

In `settings.gradle.kts`, the `repositories` block likely already uses `mavenCentral()` and `google()`.

Add JitPack to `settings.gradle.kts` → `dependencyResolutionManagement.repositories`:

```kotlin
maven { url = uri("https://jitpack.io") }
```

So it looks like:

```kotlin
repositories {
    google()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }  // ← ADD THIS LINE
}
```

---

### Step 3: Sync Gradle

- Click **File → Sync Project with Gradle Files**
- Or click the 🐘 elephant icon in the toolbar
- Wait for **"BUILD SUCCESSFUL"** in the Build output

---

### Step 4: Run the App

1. Select your emulator/device from the device dropdown (top toolbar)
2. Click the ▶️ **Run** button (or press `Shift+F10`)
3. The app will build and launch on your device

---

### Step 5: Using the App

1. **Login / Signup** — Create a parent profile with your baby's name, gender, and date of birth
2. **Home Tab** — View today's feeding summary, today's parenting tip, and quick stats
3. **Log Tab** — Record breastfeed sessions (left/right duration) or bottle feeds (formula/milk/water in ml/oz); view feeding history charts
4. **Milestones Tab** — Track cognitive, language, motor, and social developmental milestones; filter by category
5. **Health Tab** — Monitor vaccination schedule (Overdue / Upcoming / Done), track growth metrics (Weight, Height, Head circumference) with charts
6. **Guide Tab** — Browse and search evidence-based parenting guide articles

---

## 🏗️ Architecture: MVVM + Clean Architecture

```
UI Layer (Compose Screens)
    ↕ observes StateFlow
ViewModel Layer (ShishuViewModel, AuthViewModel)
    ↕ calls
Repository Layer (ShishuRepository, FeedingRepository, ShishuDbRepository)
    ↕ queries
Data Layer (Room DB → SQLite + DataStore)
```

---

## 🗄️ Database Schema

### `breastfeed_sessions` table

| Column          | Type    | Notes                          |
|-----------------|---------|-------------------------------|
| id              | Int (PK)| Auto-generated                |
| leftMinutes     | Int     | Left side duration (minutes)  |
| leftSeconds     | Int     | Left side duration (seconds)  |
| rightMinutes    | Int     | Right side duration (minutes) |
| rightSeconds    | Int     | Right side duration (seconds) |
| estimatedMl     | Int     | Estimated milk in ml          |
| noteTag         | String  | Session note/tag              |
| timestampMs     | Long    | Epoch milliseconds            |

### `bottle_sessions` table

| Column          | Type    | Notes                              |
|-----------------|---------|------------------------------------|
| id              | Int (PK)| Auto-generated                     |
| contentType     | String  | FORMULA / BREAST_MILK / WATER      |
| amountOz        | Float   | Amount in ounces                   |
| amountMl        | Int     | Amount in millilitres              |
| durationMinutes | Int     | Session duration                   |
| timestampMs     | Long    | Epoch milliseconds                 |

### `growth_records` table

| Column      | Type    | Notes                          |
|-------------|---------|-------------------------------|
| id          | Int (PK)| Auto-generated                |
| metric      | String  | WEIGHT / HEIGHT / HEAD        |
| value       | Float   | e.g. 5600 (g), 60.0 (cm)     |
| unit        | String  | "g" or "cm"                  |
| timestampMs | Long    | Epoch milliseconds            |

---

## 📦 Key Libraries Used

| Library                      | Purpose                                    |
|------------------------------|--------------------------------------------|
| Room 2.x + KSP               | Local SQLite database with DAO pattern     |
| LiveData + ViewModel         | Reactive UI updates via StateFlow          |
| Navigation Compose           | Screen navigation with animated transitions|
| MPAndroidChart 3.1.0         | Bar and line charts for feeding/growth     |
| Kotlin Coroutines            | Async database and background operations  |
| WorkManager                  | Vaccination reminder background workers   |
| Material3 + Material Icons   | Modern UI widgets and extended icon set   |
| DataStore Preferences        | Persistent auth/user preference storage   |
| Jetpack Compose BOM          | Unified Compose version management        |

---

## 🔔 Permissions

| Permission                        | Reason                             |
|-----------------------------------|------------------------------------|
| `POST_NOTIFICATIONS`              | Vaccination and feeding reminders  |
| `RECEIVE_BOOT_COMPLETED`          | Re-schedule reminders after reboot |

---

## 🛠️ Troubleshooting

| Issue                      | Fix                                                                 |
|----------------------------|---------------------------------------------------------------------|
| Gradle sync fails           | Add `maven { url = uri("https://jitpack.io") }` to `settings.gradle.kts` |
| Cannot resolve symbol errors| File → Sync Project with Gradle Files                              |
| App crashes on launch       | Check Logcat for Room migration errors; clear app data             |
| Chart not visible           | Ensure MPAndroidChart dependency is resolved via JitPack           |
| KSP errors                  | Ensure `ksp` plugin version matches Kotlin version in `build.gradle.kts` |
| DataStore/prefs not saving  | Ensure `UserPreferences` context is applicationContext, not Activity |

---

## ✅ Tested On

- Android 8.0 (API 26) and above
- Emulator: Pixel 6 API 34
- compileSdk: 35 | targetSdk: 35 | minSdk: 26

---


