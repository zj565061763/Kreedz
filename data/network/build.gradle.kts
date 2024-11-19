plugins {
   alias(libs.plugins.android.library)
   alias(libs.plugins.kotlin.android)
}

android {
   namespace = "com.sd.android.kreedz.data.network"
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
   api(libs.squareup.okhttp)
   api(libs.squareup.retrofit)
   implementation(libs.squareup.okhttp.logging)
   implementation(libs.squareup.retrofit.converter.moshi)
   implementation(libs.ext.persistentCookieJar)

   implementation(libs.didi.drouter.api)
   implementation(libs.sd.xlog)
   implementation(libs.sd.moshi)
   implementation(libs.sd.coroutines)
   implementation(libs.sd.lifecycle)
   implementation(libs.sd.retryKtx)
   implementation(libs.sd.event)
}