// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        maven(url = "https://maven.aliyun.com/repository/central")
        maven(url = "https://maven.aliyun.com/repository/public/")
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }

    allprojects {
        repositories {
            maven(url = "https://maven.aliyun.com/repository/central")
            maven(url = "https://maven.aliyun.com/repository/public/")
            maven(url = "https://maven.aliyun.com/repository/google")
            maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
            maven(url = "https://jitpack.io")
        }
    }
}

