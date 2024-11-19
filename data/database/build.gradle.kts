plugins {
   alias(libs.plugins.android.library)
   alias(libs.plugins.kotlin.android)
   alias(libs.plugins.ksp)
}

android {
   namespace = "com.sd.android.kreedz.data.database"
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
   api(libs.androidx.room.runtime)
   api(libs.androidx.room.ktx)
   api(libs.androidx.room.paging)
   ksp(libs.androidx.room.compiler)

   implementation(libs.sd.xlog)
   implementation(libs.sd.coroutines)
}