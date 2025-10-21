# Deployment Guide

This guide covers deploying the Fennec Do backend to various hosting platforms.

## ⚠️ IMPORTANT: Persistent Storage for SQLite

Your backend uses SQLite, which requires **persistent disk storage** to keep your todos and tags between restarts.

**Free options with persistent storage:**
- ✅ **Fly.io** - 1GB volume FREE
- ✅ **Railway** - $5 credit/month (includes storage)

**Options requiring payment for storage:**
- ⚠️ **Render** - Free tier WITHOUT persistent disk (costs $1/month extra)
- ❌ **Vercel/Netlify** - Not suitable for SQLite backends

---

## Recommended Options

### 1. Fly.io (Best Free Option)

**Pros:**
- ✅ Completely FREE with 1GB persistent volume
- Excellent for persistent data (SQLite databases)
- Deploy globally with edge locations
- Great performance, no cold starts
- No credit card required for free tier
- Automatic HTTPS

**Cons:**
- Requires CLI installation
- Slightly more setup than web-only platforms

**Steps:**

1. Install Fly CLI:
   ```bash
   curl -L https://fly.io/install.sh | sh
   ```

2. Sign up and login:
   ```bash
   fly auth signup
   # or
   fly auth login
   ```

3. Navigate to backend folder:
   ```bash
   cd backend
   ```

4. Launch app:
   ```bash
   fly launch --name fennec-do-backend --no-deploy
   ```
   - Choose your region when prompted
   - Say NO to PostgreSQL database
   - Say NO to Redis

5. Create persistent volume (1GB free):
   ```bash
   fly volumes create fennec_data --size 1
   ```

6. Verify fly.toml includes volume mount (already configured):
   ```toml
   [[mounts]]
     source = "fennec_data"
     destination = "/data"
   ```

7. Set database path environment variable:
   ```bash
   fly secrets set DB_PATH=/data/fennec-do.db
   ```

8. Deploy:
   ```bash
   fly deploy
   ```

9. Get your URL:
   ```bash
   fly status
   # Your app will be at: https://fennec-do-backend.fly.dev
   ```

**Useful commands:**
```bash
fly logs                  # View logs
fly status                # Check app status
fly volumes list          # Check your volume
fly ssh console           # SSH into your app
fly deploy                # Deploy updates
```

**Update your apps:**
- Web: Change API_URL in `webapp/src/services/api.js` to `https://fennec-do-backend.fly.dev/api`
- Android: Change BASE_URL in `android/app/src/main/java/com/fennecdo/api/ApiService.kt`

---

### 2. Railway (Easiest Setup)

**Pros:**
- $5 free credit per month (lasts about 1 month of usage)
- Great developer experience
- Automatic HTTPS
- Persistent storage included
- Fast deployments
- Auto-deploys on git push

**Cons:**
- Free tier limited by credit usage (typically runs out after 1 month)
- Need credit card for trial (no charges on free tier)

**Steps:**

1. Go to [railway.app](https://railway.app) and sign up
2. Click **New Project** → **Deploy from GitHub repo**
3. Select your repository
4. Railway will auto-detect Node.js
5. Configure settings:
   - **Root Directory**: `backend`
   - **Start Command**: `npm start` (usually auto-detected)
6. Add environment variables (optional):
   - `NODE_ENV`: `production`
   - `PORT`: Railway auto-assigns this
7. Click **Deploy**
8. Wait for deployment to complete
9. Click **Settings** → **Generate Domain** to get your public URL

**Update your apps:**
- Web: Change API_URL to `https://your-app-name.up.railway.app/api`
- Android: Change BASE_URL to `https://your-app-name.up.railway.app/api/`

**Note:** Storage is automatic - Railway provides persistent storage by default!

---

### 3. Render (Web-Based but Costs $1/month for Storage)

**Pros:**
- Free tier available for the web service
- Automatic deploys from GitHub
- HTTPS included
- Easy web-based setup
- Good documentation

**Cons:**
- ⚠️ **Persistent disk costs $1/month extra** (not included in free tier!)
- Free tier spins down after 15 minutes of inactivity (cold starts)
- Database will be lost without persistent disk

**Steps:**

1. Go to [render.com](https://render.com) and sign up
2. Click **New +** → **Web Service**
3. Connect your GitHub repository
4. Configure:
   - **Name**: fennec-do-backend
   - **Root Directory**: `backend`
   - **Environment**: Node
   - **Build Command**: `npm install`
   - **Start Command**: `npm start`
   - **Plan**: Free
5. ⚠️ **IMPORTANT:** Click **Advanced** → **Add Disk**
   - **Name**: fennec-data
   - **Mount Path**: `/opt/render/project/src/backend`
   - **Size**: 1 GB
   - **This costs $1/month** but is required for data persistence
6. Click **Create Web Service**
7. Wait for deployment to complete
8. Your API will be at: `https://fennec-do-backend.onrender.com`

**Update your apps:**
- Web: Change API_URL in `webapp/src/services/api.js`
- Android: Change BASE_URL in `android/app/src/main/java/com/fennecdo/api/ApiService.kt`

---

### 4. DigitalOcean App Platform

**Pros:**
- Reliable infrastructure
- Good documentation
- Scalable
- Includes persistent storage

**Cons:**
- No free tier ($5/month minimum)
- More expensive than alternatives

**Steps:**

1. Sign up at [digitalocean.com](https://www.digitalocean.com/)
2. Go to **Apps** → **Create App**
3. Connect your GitHub repository
4. Configure:
   - **Source Directory**: `backend`
   - **Environment**: Node.js
   - **Build Command**: `npm install`
   - **Run Command**: `npm start`
5. Choose $5/month Basic plan
6. Review and create
7. Your app will be deployed with persistent storage included

---

### 5. Self-Hosted (VPS)

**Best for:** Full control, learning, production at scale

**Cost:** $4-6/month

**Providers:**
- DigitalOcean Droplet ($6/month)
- Linode ($5/month)
- Vultr ($2.50-6/month)
- Hetzner ($4/month - EU)
- AWS EC2 (varies)

**Quick Setup (Ubuntu 22.04):**

```bash
# SSH into your server
ssh root@your-server-ip

# Update system
apt update && apt upgrade -y

# Install Node.js 18
curl -fsSL https://deb.nodesource.com/setup_18.x | bash -
apt install -y nodejs

# Install PM2 (process manager)
npm install -g pm2

# Clone your repository
git clone https://github.com/yourusername/fennec-do.git
cd fennec-do/backend

# Install dependencies
npm install

# Start with PM2
pm2 start src/index.js --name fennec-do-backend
pm2 startup
pm2 save

# Install and configure Nginx
apt install -y nginx

# Create Nginx config
nano /etc/nginx/sites-available/fennec-do
```

Add this configuration:

```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Continue setup:

```bash
# Enable site
ln -s /etc/nginx/sites-available/fennec-do /etc/nginx/sites-enabled/
nginx -t
systemctl restart nginx

# Setup SSL with Let's Encrypt (free HTTPS)
apt install -y certbot python3-certbot-nginx
certbot --nginx -d your-domain.com

# Your API is now live at https://your-domain.com
```

**Maintaining your VPS:**

```bash
# Update your app
cd fennec-do/backend
git pull
npm install
pm2 restart fennec-do-backend

# View logs
pm2 logs fennec-do-backend

# Monitor
pm2 monit
```

---

## After Deployment

### Update API URLs in Your Apps

**Web App** (`webapp/src/services/api.js`):
```javascript
const API_URL = 'https://your-backend-url.com/api';
```

**Android App** (`android/app/src/main/java/com/fennecdo/api/ApiService.kt`):
```kotlin
private const val BASE_URL = "https://your-backend-url.com/api/"
```

### Test Your Deployment

```bash
# Health check
curl https://your-backend-url.com/health
# Should return: {"status":"ok","timestamp":"..."}

# Check API info
curl https://your-backend-url.com/
# Should return API information

# Test sync endpoint
curl https://your-backend-url.com/api/sync
# Should return: {"todos":[],"tags":[]}
```

### Rebuild and Redeploy Your Apps

1. **Web App**:
   ```bash
   cd webapp
   npm run build
   # Deploy the dist folder to Netlify, Vercel, or GitHub Pages
   ```

2. **Android App**:
   - Commit the API URL change
   - Push to GitHub
   - GitHub Actions will build a new APK automatically
   - Download from Actions → Artifacts

---

## Environment Variables

For production, set these environment variables on your platform:

```bash
NODE_ENV=production
PORT=3000                                    # Auto-set by most platforms
CORS_ORIGIN=https://your-webapp-url.com      # Optional: restrict CORS
DB_PATH=/data/fennec-do.db                   # For Fly.io volumes
```

### Setting Environment Variables

**Fly.io:**
```bash
fly secrets set NODE_ENV=production
fly secrets set CORS_ORIGIN=https://your-app.com
```

**Railway:**
- Go to Variables tab in dashboard
- Add key-value pairs

**Render:**
- Go to Environment tab
- Add environment variables

---

## Database Backups

### Automated Backup Script

Create `backup.sh`:

```bash
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="$HOME/fennec-backups"
mkdir -p $BACKUP_DIR

# Copy database
cp backend/fennec-do.db "$BACKUP_DIR/fennec-do-$DATE.db"

# Keep only last 7 days
find $BACKUP_DIR -name "fennec-do-*.db" -mtime +7 -delete

echo "Backup completed: fennec-do-$DATE.db"
```

Make executable and add to cron:
```bash
chmod +x backup.sh
crontab -e
# Add: 0 2 * * * /path/to/backup.sh  # Daily at 2 AM
```

### Platform-Specific Backups

**Fly.io:**
```bash
# Download database from volume
fly ssh console
cd /data
cat fennec-do.db | base64  # Copy output
# On local machine:
echo "paste-base64-here" | base64 -d > backup.db
```

**Railway/Render:**
- Use platform's built-in backup features
- Or download via SSH/SFTP

---

## Monitoring & Logging

### Free Monitoring Tools

- **UptimeRobot** - https://uptimerobot.com (free tier: 50 monitors)
- **Sentry** - https://sentry.io (error tracking, free tier)
- **Logtail** - https://logtail.com (log management)
- **Papertrail** - https://papertrailapp.com (log aggregation)

### Platform Logs

**Fly.io:**
```bash
fly logs
fly logs --follow  # Real-time
```

**Railway/Render:**
- View in dashboard Logs tab

---

## Cost Comparison

| Platform | Free Tier | Persistent Storage | Monthly Cost | Best For |
|----------|-----------|-------------------|--------------|----------|
| **Fly.io** | Yes | 1GB volume FREE | $0 | Best free option |
| **Railway** | $5 credit | Included | ~$0-5 | Easiest setup |
| **Render** | Yes (with limits) | $1/month extra | $1 | Web-based UI |
| **DigitalOcean** | No | Included | $5+ | Production apps |
| **VPS** | No | Unlimited | $4-6 | Full control |

---

## Recommended Path

**For personal use (FREE):**
1. ✅ **Start with Fly.io** - completely free with persistent storage
2. Use Railway if you want easier setup ($5 credit lasts ~1 month)
3. Pay $1/month for Render if you prefer web UI

**For production/business:**
1. **Start with Railway** or **DigitalOcean App Platform**
2. **Scale to VPS** with Docker when you need more control
3. **Move to Kubernetes** for large-scale apps

---

## Troubleshooting

### Common Issues

**"Cannot connect to backend"**
```bash
# Test connectivity
curl https://your-backend-url.com/health

# Check logs
fly logs              # Fly.io
# or check dashboard for Railway/Render
```

**"Data not persisting / Database resets"**
- **Fly.io**: Verify volume exists with `fly volumes list`
- **Railway**: Storage is automatic, check logs for errors
- **Render**: Ensure persistent disk is attached (costs $1/month)
- Check DB_PATH environment variable

**"App crashes on startup"**
- Check logs for error messages
- Verify Node.js version (needs 18+)
- Ensure all dependencies are in package.json
- Check PORT environment variable

**"CORS errors in browser"**
- Set CORS_ORIGIN environment variable to your web app URL
- Or use `*` for development (not recommended for production)

**"Slow performance / Cold starts"**
- Render free tier sleeps after 15 min (first request is slow)
- Fly.io/Railway don't have this issue on free tier
- Use uptime monitoring to keep app awake (UptimeRobot)

---

## Security Best Practices

1. **Use HTTPS** - All platforms provide this automatically
2. **Set CORS properly** - Restrict to your frontend domain
3. **Keep dependencies updated** - Run `npm audit` regularly
4. **Backup your database** - Set up automated backups
5. **Monitor errors** - Use Sentry or similar
6. **Use environment variables** - Never hardcode secrets

---

## Need Help?

**Documentation:**
- Fly.io: https://fly.io/docs/
- Railway: https://docs.railway.app/
- Render: https://render.com/docs/

**Community:**
- Fly.io Discord: https://fly.io/discord
- Railway Discord: https://discord.gg/railway

**Testing:**
```bash
# Always test these endpoints after deployment
curl https://your-url.com/health
curl https://your-url.com/api/sync
curl https://your-url.com/api/todos
```
