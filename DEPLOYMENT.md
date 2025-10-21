# Deployment Guide

This guide covers deploying the Fennec Do backend to various hosting platforms.

## Recommended Options

### 1. Render (Easiest - Recommended)

**Pros:**
- Free tier available
- Automatic deploys from GitHub
- Persistent disk for SQLite database
- HTTPS included
- Easy environment variable management

**Cons:**
- Free tier spins down after 15 minutes of inactivity (cold starts)

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
5. Click **Advanced** → **Add Disk**
   - **Mount Path**: `/opt/render/project/src/backend`
   - This ensures your SQLite database persists
6. Click **Create Web Service**
7. Wait for deployment to complete
8. Your API will be available at: `https://fennec-do-backend.onrender.com`

**Update your apps:**
- Web: Change API_URL in `webapp/src/services/api.js`
- Android: Change BASE_URL in `android/app/src/main/java/com/fennecdo/api/ApiService.kt`

---

### 2. Railway

**Pros:**
- $5 free credit per month
- Great developer experience
- Automatic HTTPS
- Easy database persistence
- Fast deployments

**Cons:**
- Free tier limited by credit usage

**Steps:**

1. Go to [railway.app](https://railway.app) and sign up
2. Click **New Project** → **Deploy from GitHub repo**
3. Select your repository
4. Railway will auto-detect Node.js
5. Add environment variable:
   - `PORT`: 3000
6. In Settings:
   - **Root Directory**: `backend`
   - **Start Command**: `npm start`
7. Click **Deploy**
8. Get your public URL from the deployment

---

### 3. Fly.io

**Pros:**
- Generous free tier
- Excellent for persistent data
- Deploy globally
- Great performance

**Cons:**
- Requires Fly CLI installation
- Slightly more complex setup

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
   fly launch
   ```
   - Choose app name
   - Select region
   - Don't deploy yet

5. The `fly.toml` file has been created for you

6. Create volume for database:
   ```bash
   fly volumes create fennec_data --size 1
   ```

7. Deploy:
   ```bash
   fly deploy
   ```

8. Get your URL:
   ```bash
   fly status
   ```

---

### 4. DigitalOcean App Platform

**Pros:**
- Reliable infrastructure
- Good documentation
- Scalable

**Cons:**
- No free tier ($5/month minimum)

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

---

### 5. Self-Hosted (VPS)

**Best for:** Full control, learning, production

**Providers:**
- DigitalOcean Droplet ($4-6/month)
- Linode ($5/month)
- Vultr ($2.50-6/month)
- AWS EC2 (varies)

**Quick Setup (Ubuntu):**

```bash
# SSH into your server
ssh root@your-server-ip

# Update system
apt update && apt upgrade -y

# Install Node.js
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

# Add this configuration:
```

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
    }
}
```

```bash
# Enable site
ln -s /etc/nginx/sites-available/fennec-do /etc/nginx/sites-enabled/
nginx -t
systemctl restart nginx

# Setup SSL with Let's Encrypt
apt install -y certbot python3-certbot-nginx
certbot --nginx -d your-domain.com
```

---

## After Deployment

### Update API URLs

**Web App** (`webapp/src/services/api.js`):
```javascript
const API_URL = 'https://your-backend-url.com/api';
```

**Android App** (`android/app/src/main/java/com/fennecdo/api/ApiService.kt`):
```kotlin
private const val BASE_URL = "https://your-backend-url.com/api/"
```

### Rebuild and Redeploy

After updating the URLs:

1. **Web App**:
   ```bash
   cd webapp
   npm run build
   ```
   Deploy the `dist` folder to Netlify, Vercel, or GitHub Pages

2. **Android App**:
   - Commit the API URL change
   - Push to GitHub
   - GitHub Actions will build a new APK automatically

---

## Environment Variables

For production, consider adding these environment variables:

```bash
NODE_ENV=production
PORT=3000
CORS_ORIGIN=https://your-webapp-url.com
```

Update `backend/src/index.js` to use these:

```javascript
app.use(cors({
  origin: process.env.CORS_ORIGIN || '*'
}));
```

---

## Database Backups

For SQLite database, set up regular backups:

```bash
# Simple backup script
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
cp backend/fennec-do.db backups/fennec-do-$DATE.db
```

Or use automated backup services provided by your hosting platform.

---

## Monitoring

Consider adding:
- **Uptime monitoring**: [UptimeRobot](https://uptimerobot.com) (free)
- **Error tracking**: [Sentry](https://sentry.io) (free tier)
- **Logging**: [Logtail](https://logtail.com), [Papertrail](https://papertrailapp.com)

---

## Cost Comparison

| Platform | Free Tier | Paid Plans | Best For |
|----------|-----------|------------|----------|
| Render | Yes (with limits) | $7/month+ | Quick start, hobby projects |
| Railway | $5 credit/month | $10/month+ | Developers, quick deploys |
| Fly.io | Yes (generous) | $1.94/month+ | Performance, global apps |
| DigitalOcean | No | $5/month+ | Reliability, scaling |
| VPS | No | $4-6/month | Full control, learning |

---

## Recommended Path

For personal use:
1. **Start with Render** (free) - easiest setup
2. **Upgrade to Railway or Fly.io** if you need better performance
3. **Move to VPS** when you want full control

For production/scaling:
1. **Start with Railway or Fly.io**
2. **Move to DigitalOcean App Platform** for managed scaling
3. **Use VPS cluster** with Docker for maximum control

---

## Need Help?

Check the deployment logs:
- Render: Dashboard → Logs tab
- Railway: Deployment → View Logs
- Fly.io: `fly logs`

Common issues:
- **Port binding**: Ensure app listens on `process.env.PORT`
- **Database path**: Use absolute paths for SQLite
- **CORS errors**: Update CORS configuration
- **Cold starts**: Expect delay on free tiers after inactivity
