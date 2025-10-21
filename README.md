# Fennec Do

A modern, colorful todo list Android application with local storage.

## Features

- ✅ Colorful custom tags for organizing todos
- ✅ Priority levels (High, Medium, Low)
- ✅ Local SQLite storage (no internet required!)
- ✅ Modern Material Design 3 interface
- ✅ Completely offline and private

## Quick Start

### Download APK

Get the latest APK from GitHub Actions:

1. Go to the **[Actions tab](../../actions)**
2. Click **Android Build** → **Run workflow**
3. Wait for the build to complete
4. Download the APK from **Artifacts**
5. Install on your Android device (requires Android 7.0+)

📱 See [ANDROID_README.md](ANDROID_README.md) for detailed Android app guide

## Android App

### Features

- **Priority Sections**: Todos automatically organized by High, Medium, and Low priority
- **Colorful Tags**: Create custom tags with your choice of colors
- **Offline First**: All data stored locally on your device using Room database
- **Material Design 3**: Modern, beautiful Android UI
- **No Permissions**: Doesn't require internet or any special permissions

### Build from Source

Requirements:
- Android Studio
- JDK 8 or higher

Steps:
```bash
git clone https://github.com/yourusername/fennec-do.git
cd fennec-do
# Open the 'android' folder in Android Studio
# Click Run
```

## Tech Stack

### Android App (`/android`)
- **Language**: Kotlin
- **UI**: Material Design 3
- **Database**: Room (SQLite)
- **Architecture**: Repository pattern
- **Async**: Kotlin Coroutines
- **Version**: 2.0

## Database Schema

**Todos**
- id: unique identifier (UUID)
- title: todo text
- priority: high | medium | low
- completed: boolean
- createdAt: timestamp
- updatedAt: timestamp

**Tags**
- id: unique identifier (UUID)
- name: tag name
- color: hex color code
- createdAt: timestamp
- updatedAt: timestamp

**Todo-Tag Relationship**
- Many-to-many relationship between todos and tags

## Legacy Backend & Web App

The repository also contains a Node.js backend and React web app for reference.
These are **not required** for the Android app to function.

- Backend: `/backend` - Node.js + Express + SQLite
- Web App: `/webapp` - React + Vite

See [DEPLOYMENT.md](DEPLOYMENT.md) if you want to set up the backend for syncing across devices.

## Screenshots

(Add screenshots of your app here)

## Contributing

Feel free to open issues or submit pull requests!

## License

MIT

## Privacy

Fennec Do stores all your data locally on your device. No data is sent to external servers. Your todos are completely private.
