# Getting Started with Fennec Do

Welcome to Fennec Do - your modern, colorful cross-platform todo list!

## Quick Start Guide

### 1. Start the Backend Server

First, you need to start the backend API that both the web and Android apps will connect to.

```bash
cd backend
npm install
npm start
```

The server will start on `http://localhost:3000`

### 2. Run the Web App

In a new terminal:

```bash
cd webapp
npm install
npm run dev
```

The web app will be available at `http://localhost:5173`

### 3. Run the Android App

1. Open Android Studio
2. Select "Open Project"
3. Navigate to the `android` folder
4. Wait for Gradle sync to complete
5. Click the "Run" button or press Shift+F10

**Note:** The Android app is configured to connect to `http://10.0.2.2:3000` which is the Android emulator's way of accessing localhost on your machine. If you're using a physical device, you'll need to:
- Update the `BASE_URL` in `android/app/src/main/java/com/fennecdo/api/ApiService.kt`
- Change it to your computer's local IP address (e.g., `http://192.168.1.100:3000/api/`)

## Features Overview

### Priority Levels
- **High Priority**: For urgent tasks (displayed in red/pink gradient)
- **Medium Priority**: For important tasks (displayed in orange/yellow gradient)
- **Low Priority**: For less urgent tasks (displayed in teal/pink gradient)

### Tags
- Create colorful tags to organize your todos
- Choose custom colors using the color picker
- Tags are synced across all platforms

### Syncing
Both the web and Android apps sync in real-time with the backend:
- Click the sync button (🔄) to refresh data
- All changes are automatically saved to the backend
- Data persists in a SQLite database

## Usage Tips

### Creating a Todo
1. Enter your task in the input field
2. Select a priority level (High, Medium, or Low)
3. Click "Add Todo"

### Managing Tags
1. In the tag management section, enter a tag name
2. Choose a color using the color picker
3. Click "Add Tag"
4. Tags can be deleted by clicking the × button

### Completing Todos
- Check the checkbox next to a todo to mark it as complete
- Completed todos will show strikethrough text
- Uncheck to mark as incomplete

### Deleting Todos
- Click the "Delete" button on any todo to remove it

## Architecture

### Backend (Node.js)
- Express REST API
- SQLite database for persistence
- CORS enabled for web app access

### Web App (React)
- Modern gradient UI design
- Responsive layout
- Component-based architecture

### Android App (Kotlin)
- Material Design 3
- RecyclerView for efficient list rendering
- Coroutines for async networking

## Troubleshooting

### Web app can't connect to backend
- Ensure the backend server is running on port 3000
- Check browser console for CORS errors

### Android app can't connect to backend
- Ensure you're using the emulator or have updated the IP address
- Check that `usesCleartextTraffic` is set to true in AndroidManifest.xml (for development)
- Verify the backend is accessible from your device

### Data not syncing
- Click the sync button to manually refresh
- Check that the backend server is running
- Verify network connectivity

## Development Notes

### Backend Development
To enable hot reload during development:
```bash
npm run dev
```

### Web App Development
Vite provides hot module replacement by default when running:
```bash
npm run dev
```

### Android Development
- The app uses Retrofit for networking
- Coroutines handle asynchronous operations
- Update the API base URL for production deployments

## Future Enhancements

Potential features to add:
- User authentication
- Due dates for todos
- Reminders and notifications
- Dark mode
- Todo search and filtering
- Export/import functionality
- Collaboration features

Enjoy organizing your tasks with Fennec Do!
