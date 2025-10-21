# Fennec Do

A modern, colorful todo list application with cross-platform syncing.

## Features

- Colorful custom tags for organizing todos
- Priority levels (High, Medium, Low)
- Real-time sync between Web and Android apps
- Modern, clean interface
- Personal use optimized

## Architecture

### Backend (`/backend`)
- Node.js + Express REST API
- SQLite database for data persistence
- RESTful endpoints for todos and tags
- Sync mechanism for cross-platform support

### Web App (`/webapp`)
- React + Vite
- Responsive design
- Priority-based todo sections
- Colorful tag system

### Android App (`/android`)
- Kotlin
- Material Design 3
- Background sync
- Native Android experience

## Getting Started

See [GETTING_STARTED.md](GETTING_STARTED.md) for detailed setup instructions.

### Quick Start

**Backend:**
```bash
cd backend
npm install
npm start
```

**Web App:**
```bash
cd webapp
npm install
npm run dev
```

**Android App:**
Open the `android` folder in Android Studio and run the project.

## Deployment

Want to deploy your backend online? See:
- **[QUICK_DEPLOY.md](QUICK_DEPLOY.md)** - Fast deployment (3-5 minutes)
- **[DEPLOYMENT.md](DEPLOYMENT.md)** - Comprehensive deployment guide

### Recommended FREE Hosting (with persistent storage)

- **Fly.io** - Completely FREE with 1GB persistent volume (Recommended)
- **Railway** - $5 credit/month (includes storage, easiest setup)

⚠️ **Note:** Render's free tier does NOT include persistent storage ($1/month extra for disk)

### Download Android APK

GitHub Actions automatically builds APKs:
1. Go to **Actions** tab
2. Click **Android Build** → **Run workflow**
3. Download from **Artifacts**

## Database Schema

**Todos**
- id: unique identifier
- title: todo text
- priority: high | medium | low
- completed: boolean
- createdAt: timestamp
- updatedAt: timestamp
- tags: array of tag IDs

**Tags**
- id: unique identifier
- name: tag name
- color: hex color code

## API Endpoints

- `GET /api/todos` - Get all todos
- `POST /api/todos` - Create new todo
- `PUT /api/todos/:id` - Update todo
- `DELETE /api/todos/:id` - Delete todo
- `GET /api/tags` - Get all tags
- `POST /api/tags` - Create new tag
- `PUT /api/tags/:id` - Update tag
- `DELETE /api/tags/:id` - Delete tag
- `GET /api/sync` - Get all data (todos + tags)
