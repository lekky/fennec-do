# Quick Deploy Guide

The fastest ways to get your backend online:

## 🚀 Option 1: Render (Recommended - Easiest)

**Time: 5 minutes**

1. Sign up at [render.com](https://render.com)
2. Click **New +** → **Web Service**
3. Connect your GitHub repo
4. Settings:
   - **Name**: fennec-do-backend
   - **Root Directory**: `backend`
   - **Build Command**: `npm install`
   - **Start Command**: `npm start`
5. **Advanced** → **Add Disk**:
   - **Mount Path**: `/opt/render/project/src/backend`
6. Deploy!

Your API: `https://fennec-do-backend.onrender.com`

---

## 🚂 Option 2: Railway (Fast & Modern)

**Time: 3 minutes**

1. Sign up at [railway.app](https://railway.app)
2. **New Project** → **Deploy from GitHub**
3. Select repo
4. Settings:
   - **Root Directory**: `backend`
5. Deploy!

Get URL from dashboard.

---

## ✈️ Option 3: Fly.io (Best Performance)

**Time: 5 minutes**

```bash
# Install Fly CLI
curl -L https://fly.io/install.sh | sh

# Login
fly auth login

# Deploy (from backend directory)
cd backend
fly launch --name fennec-do-backend
fly volumes create fennec_data --size 1
fly deploy
```

---

## After Deployment

### 1. Test Your API

Visit: `https://your-backend-url.com/health`

Should return: `{"status":"ok"}`

### 2. Update Web App

Edit `webapp/src/services/api.js`:

```javascript
const API_URL = 'https://your-backend-url.com/api';
```

### 3. Update Android App

Edit `android/app/src/main/java/com/fennecdo/api/ApiService.kt`:

```kotlin
private const val BASE_URL = "https://your-backend-url.com/api/"
```

### 4. Rebuild Apps

**Web:**
```bash
cd webapp
npm run build
```

**Android:**
- Commit changes
- Push to GitHub
- Download new APK from Actions

---

## Deploy Web App (Bonus)

### Netlify (Easiest for Web)

1. Sign up at [netlify.com](https://netlify.com)
2. Drag & drop `webapp/dist` folder
3. Done!

### Vercel

```bash
cd webapp
npx vercel
```

---

## Troubleshooting

**"Cannot connect to backend"**
- Check backend is running at the URL
- Verify CORS settings
- Test with: `curl https://your-backend-url.com/health`

**"Database not persisting"**
- Ensure persistent disk/volume is configured
- Check DB_PATH environment variable

**"App crashes on startup"**
- Check logs in your platform dashboard
- Verify all dependencies in package.json
- Ensure Node.js version is 18+

---

## Cost Summary

- **Render Free**: 750 hours/month (sleeps after inactivity)
- **Railway**: $5 credit/month
- **Fly.io**: 3 VMs free (256MB RAM each)

All three are free for personal use!
