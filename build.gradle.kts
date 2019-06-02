buildscript {

    repositories {
        jcenter()
        google()
        maven { setUrl("https://maven.fabric.io/public") }
        maven { setUrl("https://jitpack.io") }
    }
    dependencies {
        classpath(AndroidDependencies.androidGradle)
        classpath(AndroidDependencies.kotlinGradle)
        classpath(AndroidDependencies.navigationSafeArgs)
    }
}

allprojects {
    repositories {
        mavenLocal()
        jcenter()
        google()
        maven { setUrl("https://maven.fabric.io/public") }
        maven { setUrl("https://jitpack.io") }
        // artifacts are published to this repository
        maven { setUrl("https://kotlin.bintray.com/kotlinx") }
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}

