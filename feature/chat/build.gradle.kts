plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.compose.compiler)
}

android {
  namespace = "com.sd.android.kreedz.feature.chat"
  compileSdk = libs.versions.androidCompileSdk.get().toInt()
  defaultConfig {
    minSdk = libs.versions.androidMinSdk.get().toInt()
    consumerProguardFiles("consumer-rules.pro")
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  implementation(projects.core)
  implementation(projects.feature.common)
}