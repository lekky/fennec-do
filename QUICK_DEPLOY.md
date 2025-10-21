# Quick Deploy Guide

The fastest ways to get your backend online **with persistent data storage**.

## ⚠️ Important: Free Tier Storage

**SQLite needs persistent storage or your data will be lost on restart!**

✅ **Free tiers WITH persistent storage:**
- Fly.io (1GB volume free)
- Railway ($5 credit/month - includes storage)

❌ **Free tiers WITHOUT persistent storage:**
- Render (persistent disk costs $1/month extra)
- Most serverless platforms

---

## ✈️ Option 1: Fly.io (RECOMMENDED - Free with Storage)

**Time: 5 minutes | FREE with 1GB persistent volume**

### Setup

```bash
# Install Fly CLI
curl -L https://fly.io/install.sh | sh

# Login (creates account if needed)
fly auth login

# Navigate to backend
cd backend

# Launch app (select region when prompted)
fly launch --name fennec-do-backend --no-deploy

# Create persistent volume (1GB free)
fly volumes create fennec_data --size 1

# Update fly.toml to use the volume
# (already configured in your repo)

# Deploy!
fly deploy
```

### Get Your URL

```bash
fly status
# or visit: https://fennec-do-backend.fly.dev
```

**Why Fly.io?**
- ✅ 1GB persistent volume FREE
- ✅ 3 shared VMs free
- ✅ No credit card required for free tier
- ✅ Auto HTTPS
- ✅ Fast global deployment

---

## 🚂 Option 2: Railway (Fast & Modern)

**Time: 3 minutes | $5 credit/month (lasts ~1 month)**

### Setup

1. Sign up at [railway.app](https://railway.app)
2. Click **New Project** → **Deploy from GitHub repo**
3. Select your `fennec-do` repository
4. Railway auto-detects Node.js
5. Add environment variables:
   - Root Directory: `backend`
6. Deploy!

**Why Railway?**
- ✅ Includes persistent storage
- ✅ Very easy to use
- ✅ $5 free credit monthly
- ✅ Great developer experience

---

## 💰 Option 3: Render ($1/month for storage)

**Time: 5 minutes | $1/month for persistent disk**

⚠️ **Note:** Render's free tier does NOT include persistent storage. You need to pay $1/month for a disk.

### Setup

1. Sign up at [render.com](https://render.com)
2. Click **New +** → **Web Service**
3. Connect your GitHub repo
4. Settings:
   - **Name**: fennec-do-backend
   - **Root Directory**: `backend`
   - **Build Command**: `npm install`
   - **Start Command**: `npm start`
   - **Plan**: Free
5. ⚠️ **Important:** Click **Advanced** → **Add Disk**:
   - **Name**: fennec-data
   - **Mount Path**: `/opt/render/project/src/backend`
   - **Size**: 1GB
   - **Cost**: $1/month
6. Deploy!

---

## 🎯 My Recommendation

**For completely FREE with persistent storage:**
→ **Use Fly.io** (Option 1)

**For easiest setup with $5 credit:**
→ **Use Railway** (Option 2)

---

## After Deployment

### 1. Test Your API

Visit in browser: `https://your-backend-url/health`

Should return:
```json
{"status":"ok","timestamp":"2025-10-21T..."}
```

### 2. Update Web App

Edit `webapp/src/services/api.js`:

```javascript
const API_URL = 'https://fennec-do-backend.fly.dev/api';
// or 'https://your-app.up.railway.app/api'
```

### 3. Update Android App

Edit `android/app/src/main/java/com/fennecdo/api/ApiService.kt`:

```kotlin
private const val BASE_URL = "https://fennec-do-backend.fly.dev/api/"
// or "https://your-app.up.railway.app/api/"
```

### 4. Rebuild Apps

**Web App:**
```bash
cd webapp
npm run build
# Deploy to Netlify or Vercel (see below)
```

**Android App:**
1. Commit the API URL change
2. Push to GitHub
3. Go to Actions tab → Run "Android Build" workflow
4. Download APK from Artifacts

---

## Deploy Web App (Bonus)

Both Netlify and Vercel have generous free tiers:

### Netlify (Drag & Drop)

1. Build your app: `cd webapp && npm run build`
2. Go to [netlify.com](https://netlify.com)
3. Drag the `dist` folder to Netlify
4. Done! You get a URL like `https://your-app.netlify.app`

### Vercel (CLI)

```bash
cd webapp
npm install -g vercel
vercel
```

Follow prompts and get URL like `https://your-app.vercel.app`

---

## Troubleshooting

### "Cannot connect to backend"
```bash
# Test your backend directly
curl https://your-backend-url/health

# Should return: {"status":"ok",...}
```

**If it fails:**
- Check deployment logs on Fly.io/Railway dashboard
- Verify the URL is correct
- Check CORS settings in backend

### "Data not persisting"
- **Fly.io**: Ensure volume was created with `fly volumes list`
- **Railway**: Storage is automatic, check logs
- **Render**: Verify persistent disk is attached (costs $1/month)

### "App crashes on startup"
```bash
# Check logs
fly logs          # for Fly.io
# or check Railway/Render dashboard
```

Common issues:
- Missing dependencies in package.json
- Wrong Node.js version (needs 18+)
- Database path issues

---

## Cost Comparison

| Platform | Free Tier | Storage | Cold Starts | Best For |
|----------|-----------|---------|-------------|----------|
| **Fly.io** | Yes | 1GB free volume | No | Free + persistent storage |
| **Railway** | $5 credit/mo | Included | No | Easy setup, generous credit |
| **Render** | Yes | $1/month disk | After 15 min idle | Don't want CLI |

---

## Updating Your Deployment

After making code changes:

**Fly.io:**
```bash
cd backend
fly deploy
```

**Railway:**
- Just `git push` - auto-deploys!

**Render:**
- Just `git push` - auto-deploys!

---

## Need Help?

**Fly.io Docs:** https://fly.io/docs/
**Railway Docs:** https://docs.railway.app/
**Render Docs:** https://render.com/docs/

**Common Commands:**

```bash
# Fly.io
fly logs              # View logs
fly status            # Check status
fly volumes list      # Check storage
fly ssh console       # SSH into app

# Test your API
curl https://your-backend-url/health
curl https://your-backend-url/api/sync
```
