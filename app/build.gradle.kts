plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.googleServices)
  alias(libs.plugins.firebase.crashlytics)
  id("com.didi.drouter")
}

android {
  namespace = "com.sd.android.kreedz"
  compileSdk = libs.versions.androidCompileSdk.get().toInt()
  defaultConfig {
    targetSdk = libs.versions.androidTargetSdk.get().toInt()
    minSdk = libs.versions.androidMinSdk.get().toInt()
    applicationId = "com.sd.android.kreedz"
    versionCode = 1
    versionName = "1.0"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }

    buildConfigField("String", "HOST", """"xtreme-jumps.eu"""")
  }

  //签名
  signingConfigs {
    create("release") {
      storeFile = file("template.jks")
      storePassword = "template"
      keyAlias = "template"
      keyPassword = "template"
    }
  }

  buildTypes {
    debug {
      signingConfig = signingConfigs["release"]
    }
    release {
      signingConfig = signingConfigs["release"]
      isShrinkResources = false
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  kotlinOptions {
    jvmTarget = "1.8"
  }

  buildFeatures {
    buildConfig = true
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }

  flavorDimensions += "default"
  productFlavors {
    create("kreedz") {
      applicationId = "com.sd.android.kreedz"
      versionCode = 1
      versionName = "1.0.0-beta03"
      base.archivesName = "${versionName}-${versionCode}"
    }
  }
}

dependencies {
  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.analytics)
  implementation(libs.firebase.crashlytics)

  implementation(projects.core)
  implementation(projects.feature.account)
  implementation(projects.feature.blog)
  implementation(projects.feature.common)
  implementation(projects.feature.news)
  implementation(projects.feature.ranking)
  implementation(projects.feature.records)
  implementation(projects.feature.chat)
  implementation(projects.feature.more)

  debugImplementation(libs.squareup.leakcanary.android)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.androidx.test.espresso.core)
}