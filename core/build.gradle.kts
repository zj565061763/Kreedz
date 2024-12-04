plugins {
   alias(libs.plugins.android.library)
   alias(libs.plugins.kotlin.android)
   alias(libs.plugins.compose.compiler)
}

android {
   namespace = "com.sd.android.kreedz.core"
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
   api(projects.data.data)

   api(platform(libs.androidx.compose.bom))
   api(libs.androidx.compose.material3)
   api(libs.androidx.compose.ui)
   api(libs.androidx.compose.ui.tooling.preview)
   debugApi(libs.androidx.compose.ui.tooling)

   api(libs.androidx.activity.compose)
   api(libs.androidx.lifecycle.viewmodel.compose)
   api(libs.androidx.lifecycle.runtime.compose)

   api(libs.didi.drouter.api)
   api(libs.ext.coil)

   api(libs.sd.compose.active)
   api(libs.sd.compose.systemui)
   api(libs.sd.compose.tabContainer)
   api(libs.sd.compose.refresh)
   api(libs.sd.compose.input)
   api(libs.sd.compose.layer)
   api(libs.sd.compose.webview)
   api(libs.sd.compose.constraintlayout)
   api(libs.sd.compose.wheelPicker)
   api(libs.sd.compose.paging)
   api(libs.sd.compose.annotated)
   api(libs.sd.compose.html)
   api(libs.sd.compose.utils)

   api(libs.sd.ctx)
   api(libs.sd.coroutines)
   api(libs.sd.retryKtx)
   api(libs.sd.datastore)
   api(libs.sd.xlog)
   api(libs.sd.moshi)
   api(libs.sd.event)
   api(libs.sd.date)
}