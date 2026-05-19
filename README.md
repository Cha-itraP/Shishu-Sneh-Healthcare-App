🍼 Shishu-Sneh — Baby Healthcare Companion

An AI-powered Android application designed to support new mothers in tracking their baby’s first-year health, growth, and development.

📌 Problem Statement

New mothers, especially in rural and underserved areas, often lack access to reliable healthcare guidance for their baby’s early development. Important aspects like growth monitoring, vaccination schedules, and feeding practices are frequently missed due to lack of awareness or tracking tools.

Shishu-Sneh aims to solve this problem by providing a simple, accessible, and intelligent digital companion that ensures better baby care through technology.

✨ Features
📊 Real-time Growth Monitoring
Track baby’s height and weight
Visual charts using MPAndroidChart
Monthly growth analysis
💉 Vaccination Management
Automated reminders based on baby’s DOB
Information about diseases and prevention
Ensures no missed vaccinations
🥗 Personalized Feeding Guidance
Daily nutrition tips for baby and mother
Easy-to-understand recommendations
✅ Development Milestone Tracking
Track developmental activities
Simple Yes/No input system
Example: “Can the baby hold their head up?”
🔔 Smart Notifications
Automated reminders using WorkManager
Background alerts without manual tracking
🧠 AI-assisted Insights
Smart suggestions based on user data
Helps mothers make informed decisions
🛠️ Tech Stack
Category	Technology Used
Language	Java / Kotlin
Platform	Android
Architecture	MVVM
Database	Room Database (Room DB)
Charts	MPAndroidChart
Background Jobs	WorkManager
⚙️ Installation Steps

Clone the repository:

git clone https://github.com/your-username/shishu-sneh.git
Open the project in Android Studio
Sync Gradle:
Click "Sync Project with Gradle Files"
Connect an emulator or Android device
▶️ Run the App

Click the Run ▶️ button in Android Studio
or use:

Shift + F10
📸 Screenshots

Add your app screenshots here

Example:

Home Screen
Growth Chart
Vaccination Reminder
Feeding Guidance

🎥 Demo Link

Add your demo video link here (Google Drive / YouTube)

📂 Project Structure
Shishu-Sneh/
│── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/shishusneh/
│   │   │   │   ├── ui/              # Activities & Fragments
│   │   │   │   ├── viewmodel/      # MVVM ViewModels
│   │   │   │   ├── repository/     # Data handling
│   │   │   │   ├── database/       # Room DB
│   │   │   │   ├── model/          # Data models
│   │   │   │   └── utils/          # Helper classes
│   │   │   ├── res/                # Layouts, drawables, values
│   │   │   └── AndroidManifest.xml
│── build.gradle
│── settings.gradle
│── README.md

✔ Clear separation of concerns (UI, ViewModel, Repository, Database)
✔ Organized assets and resources
✔ Configuration files included (Gradle)

🔮 Future Improvements
🌐 Multi-language support (for rural accessibility)
☁️ Cloud sync & backup
🤖 Advanced AI chatbot for real-time guidance
📍 Integration with nearby healthcare centers
📊 More detailed analytics & reports

📎 Conclusion

Shishu-Sneh is a step towards leveraging technology to improve early childhood healthcare. By combining simplicity, accessibility, and AI-driven insights, the app empowers mothers to provide better care for their babies.
