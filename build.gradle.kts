buildscript {
  dependencies {
    classpath("io.github.didi:drouter-plugin:1.4.0")
  }
}

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.compose.compiler) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.googleServices) apply false
  alias(libs.plugins.firebase.crashlytics) apply false
}