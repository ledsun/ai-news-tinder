# ai-news-tinder

AIニュースを読んで振り分けるAndroidアプリケーション

## Build

PowerShell でプロジェクトルートに移動してから実行します。

```powershell
cd C:\Users\led_l\ai-news-tinder
```

### 1. 環境変数を設定

Android Studio 同梱 JBR とローカル Android SDK を使います。

```powershell
$env:JAVA_HOME = 'C:\Program Files\Android\Android Studio\jbr'
$env:PATH = "$env:JAVA_HOME\bin;C:\Users\led_l\AppData\Local\Programs\Gradle\gradle-8.9\bin;$env:PATH"
$env:ANDROID_HOME = 'C:\Users\led_l\AppData\Local\Android\Sdk'
$env:ANDROID_SDK_ROOT = $env:ANDROID_HOME
```

### 2. 必要な SDK をインストール

`cmdline-tools` が `C:\Users\led_l\AppData\Local\Android\Sdk\cmdline-tools\latest` に配置済みである前提です。

```powershell
& "$env:ANDROID_HOME\cmdline-tools\latest\bin\sdkmanager.bat" --sdk_root="$env:ANDROID_HOME" --licenses
& "$env:ANDROID_HOME\cmdline-tools\latest\bin\sdkmanager.bat" --sdk_root="$env:ANDROID_HOME" --install "platforms;android-35" "build-tools;35.0.0" "platform-tools"
```

### 3. Debug APK をビルド

```powershell
gradle assembleDebug
```

出力先:

```text
app/build/outputs/apk/debug/app-debug.apk
```

### 補足

必要なら `local.properties` に SDK パスを書きます。

```properties
sdk.dir=C:\\Users\\led_l\\AppData\\Local\\Android\\Sdk
```
