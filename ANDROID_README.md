# Fennec Do - Android App Guide

A beautiful, offline-first todo list app for Android with colorful tags and priority organization.

## Download & Install

### Download APK

1. Go to the [Actions tab](../../actions) on GitHub
2. Click **Android Build** workflow
3. Click **Run workflow** button (green)
4. Select build type: **debug** (recommended)
5. Wait 3-5 minutes for build to complete
6. Download `fennec-do-debug-apk.zip` from **Artifacts**
7. Extract the ZIP file

### Install on Android

1. Transfer the APK to your Android device
2. Open the APK file
3. If prompted, enable "Install from Unknown Sources"
4. Follow installation prompts
5. Open Fennec Do!

**Requirements**: Android 7.0 (API 24) or higher

## Features

### Priority Organization

Todos are automatically organized into three sections:

- **High Priority** - Urgent tasks (red/pink gradient badge)
- **Medium Priority** - Important tasks (orange/yellow gradient badge)
- **Low Priority** - Less urgent tasks (teal/pink gradient badge)

### Colorful Tags

Create unlimited custom tags to categorize your todos:

1. Tap the tag icon or "Manage Tags" (future feature)
2. Enter tag name
3. Pick any color you like
4. Add the tag to todos

### Completely Offline

- All data stored locally in SQLite database
- No internet connection required
- No account needed
- Your data never leaves your device
- Complete privacy

### Material Design 3

- Modern Android UI
- Smooth animations
- Color-coded priorities
- Chip-based tags
- Floating action button for refresh

## Usage

### Creating a Todo

1. Type your task in the text field
2. Select priority: High, Medium, or Low
3. Tap "Add Todo"

### Completing Todos

- Tap the checkbox next to any todo
- Completed todos show strikethrough text
- Tap again to mark as incomplete

### Deleting Todos

- Tap the "Delete" button on any todo
- Todo is immediately removed

### Refreshing

- Tap the floating refresh button (bottom right)
- Reloads all todos from database

## Data Storage

### Location

All data is stored in a local SQLite database managed by Room:

- Database name: `fennec_do_database`
- Location: `/data/data/com.fennecdo/databases/`
- Automatic backups via Android backup service

### Backup

Your todos are automatically backed up by Android if you have:
- Google account signed in
- Backup enabled in Android settings

To manually backup:
1. Use Android's built-in backup feature
2. Or use file manager apps with root access
3. Or export data (future feature)

## Technical Details

### Built With

- **Kotlin** - Modern Android development
- **Room Database** - Local SQLite storage
- **Coroutines** - Asynchronous operations
- **Material Components** - UI elements
- **View Binding** - Type-safe view access
- **RecyclerView** - Efficient list rendering

### Architecture

```
MainActivity
    ↓
LocalTodoRepository
    ↓
Room Database (AppDatabase)
    ↓
DAOs (TodoDao, TagDao, TodoTagDao)
    ↓
SQLite Database File
```

### Database Tables

**todos**
- Stores individual todo items
- Tracks completion status
- Stores priority level

**tags**
- Stores tag definitions
- Stores tag colors

**todo_tags**
- Links todos to tags
- Many-to-many relationship

### Version History

**v2.0** (Current)
- Complete rewrite with local storage
- Removed backend dependency
- Added Room database
- Offline-first architecture
- No internet permissions

**v1.0** (Legacy)
- API-based syncing
- Required backend server

## Troubleshooting

### App won't install

- Ensure Android version is 7.0 or higher
- Enable "Install from Unknown Sources"
- Check you have enough storage space

### Data disappeared

- Check if app was uninstalled (clears data)
- Restore from Android backup
- Note: Clearing app data deletes all todos

### App crashes

- Clear app cache in Android settings
- Reinstall the app
- Check Android version compatibility

### Performance issues

- Clear completed todos regularly
- Reduce number of tags per todo
- Restart the app

## Building from Source

### Requirements

- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34
- Kotlin 1.9+

### Steps

1. Clone repository:
   ```bash
   git clone https://github.com/yourusername/fennec-do.git
   ```

2. Open in Android Studio:
   - File → Open
   - Navigate to `fennec-do/android`
   - Wait for Gradle sync

3. Build:
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Or click Run button

4. Install:
   - Connect Android device via USB
   - Enable USB debugging
   - Click Run

### Gradle Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK (unsigned)
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug

# Run tests
./gradlew test
```

## Future Features

- [ ] Tag management UI
- [ ] Data export/import
- [ ] Dark mode
- [ ] Due dates
- [ ] Reminders
- [ ] Search and filter
- [ ] Sort options
- [ ] Statistics
- [ ] Widgets
- [ ] Backup to cloud storage (optional)

## Privacy Policy

Fennec Do:
- Does NOT collect any data
- Does NOT require internet connection
- Does NOT send data to servers
- Does NOT use analytics
- Does NOT require account creation
- Stores ALL data locally on your device

Your todos are completely private and never leave your device.

## Support

- Report bugs: [GitHub Issues](../../issues)
- Feature requests: [GitHub Issues](../../issues)
- Questions: [GitHub Discussions](../../discussions)

## License

MIT License - See [LICENSE](LICENSE) file for details
