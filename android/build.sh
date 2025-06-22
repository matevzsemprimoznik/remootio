#!/bin/bash

APP_BUILD_GRADLE="app/build.gradle.kts"
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
SAVED_APK_DIR="versions"
LATEST_DIR="$SAVED_APK_DIR/latest"

# Create folders if they don't exist
mkdir -p "$SAVED_APK_DIR"
mkdir -p "$LATEST_DIR"

if [ ! -f "$APP_BUILD_GRADLE" ]; then
  echo "Error: $APP_BUILD_GRADLE not found!"
  exit 1
fi

echo "Reading current versionCode and versionName from $APP_BUILD_GRADLE..."

CURRENT_VERSION_CODE=$(grep 'versionCode' "$APP_BUILD_GRADLE" | head -1 | grep -o '[0-9]\+')
CURRENT_VERSION_NAME=$(grep 'versionName' "$APP_BUILD_GRADLE" | head -1 | grep -o '".*"' | tr -d '"')

echo "Current versionCode: $CURRENT_VERSION_CODE"
echo "Current versionName: $CURRENT_VERSION_NAME"

if [[ -z "$CURRENT_VERSION_CODE" || -z "$CURRENT_VERSION_NAME" ]]; then
  echo "Error: Could not parse versionCode or versionName from $APP_BUILD_GRADLE"
  exit 1
fi

# Move existing APK to saved_apks folder with versionCode before cleaning
if [ -f "$APK_PATH" ]; then
  SAVED_APK_NAME="app_v${CURRENT_VERSION_CODE}.apk"
  echo "Saving old APK to $SAVED_APK_DIR/$SAVED_APK_NAME"
  mv "$APK_PATH" "$SAVED_APK_DIR/$SAVED_APK_NAME"
else
  echo "No existing APK found to save."
fi

# Increment versionCode
NEW_VERSION_CODE=$((CURRENT_VERSION_CODE + 1))

# Increment minor part of versionName (X.Y)
IFS='.' read -ra VER_PARTS <<< "$CURRENT_VERSION_NAME"
MAJOR=${VER_PARTS[0]}
MINOR=${VER_PARTS[1]:-0}
NEW_MINOR=$((MINOR + 1))
NEW_VERSION_NAME="$MAJOR.$NEW_MINOR"

echo "Updating versionCode to $NEW_VERSION_CODE"
echo "Updating versionName to $NEW_VERSION_NAME"

# Update versionCode and versionName in build.gradle.kts
sed -i.bak -E "s/versionCode[[:space:]]*=[[:space:]]*[0-9]+/versionCode = $NEW_VERSION_CODE/" "$APP_BUILD_GRADLE"
sed -i.bak -E "s/versionName[[:space:]]*=[[:space:]]*\"[0-9]+\.[0-9]+\"/versionName = \"$NEW_VERSION_NAME\"/" "$APP_BUILD_GRADLE"

echo "Updated $APP_BUILD_GRADLE (backup saved as $APP_BUILD_GRADLE.bak)"

echo "Running ./gradlew clean"
./gradlew clean

echo "Running ./gradlew assembleDebug"
./gradlew assembleDebug

echo "Build complete! New APK is at $APK_PATH"

# Copy new APK to versions/latest/
if [ -f "$APK_PATH" ]; then
  echo "Copying new APK to $LATEST_DIR/app-debug.apk"
  cp "$APK_PATH" "$LATEST_DIR/app-debug.apk"
else
  echo "Warning: New APK not found at $APK_PATH"
fi

METADATA_JSON="$LATEST_DIR/metadata.json"

if [ -f "$METADATA_JSON" ]; then
  echo "Updating latestVersion in $METADATA_JSON to $NEW_VERSION_CODE"
  # Use jq if available (recommended):
  if command -v jq &> /dev/null; then
    jq --argjson v "$NEW_VERSION_CODE" '.latestVersion = $v' "$METADATA_JSON" > "$METADATA_JSON.tmp" && mv "$METADATA_JSON.tmp" "$METADATA_JSON"
  else
    # Fallback with sed (assumes exact format with "latestVersion": number)
    sed -i.bak -E "s/\"latestVersion\"[[:space:]]*:[[:space:]]*[0-9]+/\"latestVersion\": $NEW_VERSION_CODE/" "$METADATA_JSON"
  fi
else
  echo "Warning: $METADATA_JSON not found, skipping update."
fi
