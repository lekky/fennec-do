# GitHub Actions Workflows

## Android Build Workflow

The `android-build.yml` workflow automatically builds the Android APK for Fennec Do.

### Triggers

The workflow runs on:
- **Push** to `main` or any `claude/*` branch
- **Pull Request** to `main`
- **Manual trigger** via workflow_dispatch (with build type selection)

### How to Download the APK

#### Option 1: Automatic Build (After Push)
1. Push your code to the repository
2. Go to the **Actions** tab in GitHub
3. Click on the latest **Android Build** workflow run
4. Scroll down to the **Artifacts** section
5. Download `fennec-do-debug-apk.zip`
6. Extract the ZIP file to get your APK

#### Option 2: Manual Build
1. Go to the **Actions** tab in GitHub
2. Click on **Android Build** in the left sidebar
3. Click **Run workflow** button (on the right)
4. Select build type:
   - **debug**: Creates a debug APK (ready to install)
   - **release**: Creates a release APK (needs signing)
5. Click **Run workflow**
6. Wait for the build to complete
7. Download the APK from the **Artifacts** section

### Build Types

- **Debug APK**:
  - Ready to install immediately
  - Larger file size
  - Includes debugging symbols
  - File name: `app-debug.apk`

- **Release APK**:
  - Optimized and smaller
  - Requires signing for production
  - File name: `app-release-unsigned.apk`

### Installing the APK on Android

1. Download the APK artifact from GitHub Actions
2. Extract the ZIP file
3. Transfer the APK to your Android device
4. Enable "Install from Unknown Sources" in your device settings
5. Open the APK file and follow the installation prompts

### Troubleshooting

**Build Fails:**
- Check the workflow logs in the Actions tab
- Ensure all Gradle files are properly committed
- Verify the Android build configuration is correct

**APK Won't Install:**
- Make sure you've enabled installation from unknown sources
- Check that you're using an Android device with API 24+ (Android 7.0+)
- Try uninstalling any previous version of the app first

### Retention

APK artifacts are kept for 30 days. After that, they are automatically deleted to save storage space.
