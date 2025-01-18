plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "com.sd.android.kreedz.data"
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
  api(projects.data.database)
  api(projects.data.network)
  api(projects.res)

  implementation(libs.ext.coil)

  implementation(libs.sd.ctx)
  implementation(libs.sd.moshi)
  implementation(libs.sd.xlog)
  implementation(libs.sd.coroutines)
  implementation(libs.sd.retryKtx)
  implementation(libs.sd.datastore)
  implementation(libs.sd.lifecycle)
}