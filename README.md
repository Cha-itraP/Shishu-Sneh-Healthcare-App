🍼 Shishu-Sneh — Baby Healthcare Companion

📱 Android App | Kotlin | Room DB | MVVM | MPAndroidChart

An AI-powered Android application to support new mothers in tracking baby health, growth, and development.

📌 Problem Statement

New mothers often struggle to track their baby’s health, vaccination schedules, and development milestones—especially in rural areas with limited access to healthcare guidance.

Shishu-Sneh provides a simple, intelligent solution to ensure proper baby care through tracking, reminders, and AI-based guidance.

✨ Features
📊 Real-time growth tracking (Height & Weight)
💉 Smart vaccination reminders
🥗 Personalized feeding guidance
✅ Milestone tracking (Yes/No system)
🔔 Automated notifications (WorkManager)
🧠 AI-assisted insights

🛠️ Tech Stack
Category	Technology Used
Language	Kotlin / Java
Platform	Android
Architecture	MVVM
Database	Room DB
Charts	MPAndroidChart
Background Jobs	WorkManager

🚀 Steps to Run in Android Studio

Prerequisites
Android Studio Hedgehog or newer
JDK 17
Android SDK installed
Emulator or physical device

Step 1: Open Project
Open Android Studio
Click File → Open
Select project folder
Click OK
Wait for Gradle sync

Step 2: Add JitPack Repository (for MPAndroidChart)

In settings.gradle:

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

Step 3: Sync Gradle
Click Sync Project with Gradle Files
Wait for BUILD SUCCESSFUL

Step 4: Run App
Select emulator/device
Click ▶️ Run button
Or press:
Shift + F10

📂 Project Structure
Shishu-Sneh/
│── app/
│   ├── src/main/
│   │   ├── java/com/example/shishusneh/
│   │   │   ├── ui/              # Activities & Fragments
│   │   │   ├── viewmodel/      # MVVM ViewModels
│   │   │   ├── repository/     # Data handling
│   │   │   ├── database/       # Room DB
│   │   │   ├── model/          # Data models
│   │   │   └── utils/          # Helper classes
│   │   ├── res/                # Layouts, drawables, values
│   │   └── AndroidManifest.xml
│── build.gradle
│── settings.gradle
│── README.md

📊 Architecture
UI Layer (Fragments / Activities)
        ↓
ViewModel (LiveData)
        ↓
Repository
        ↓
Room Database (SQLite)

📸 Screenshots

Add your app screenshots here

🎥 Demo Link

Add your demo video link (YouTube / Drive)

🔮 Future Improvements
🌐 Multi-language support
☁️ Cloud backup & sync
🤖 AI chatbot integration
📍 Nearby healthcare integration
📊 Advanced analytics

📎 Conclusion
Shishu-Sneh improves early childhood healthcare by combining accessibility, tracking, and AI-driven insights, helping mothers provide better care for their babies.
