plugins {
   alias(libs.plugins.android.library)
   alias(libs.plugins.kotlin.android)
}

android {
   namespace = "com.sd.android.kreedz.res"
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
}