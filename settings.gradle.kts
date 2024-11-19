pluginManagement {
   repositories {
      google()
      mavenCentral()
      gradlePluginPortal()
   }
}
dependencyResolutionManagement {
   repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
   repositories {
      google()
      mavenCentral()
      maven("https://jitpack.io")
   }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Kreedz"

include(":app")
include(":core")
include(":data:data")
include(":data:database")
include(":data:network")
include(":res")
include(":feature:account")
include(":feature:chat")
include(":feature:common")
include(":feature:more")
include(":feature:news")
include(":feature:ranking")
include(":feature:records")
include(":feature:template")