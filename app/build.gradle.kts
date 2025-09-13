plugins {
    id("com.diffplug.spotless") version "7.2.1"
    id("com.android.application")
    id("androidx.navigation.safeargs")
}

android {
    compileSdk = 36
    defaultConfig {
        applicationId = "org.d3kad3nt.sunriseClock"
        minSdk = 21
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform {}
        }
    }

    namespace = "org.d3kad3nt.sunriseClock"
}

dependencies {

    // Util module
    implementation(project(":util"))

    // Backend module
    implementation(project(":backend"))

    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.9.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.3")

    // Navigation component
    val navVersion: String by rootProject.extra
    implementation("androidx.navigation:navigation-fragment:$navVersion")
    implementation("androidx.navigation:navigation-ui:$navVersion")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$navVersion")

    // GUI
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.viewpager:viewpager:1.1.0")

    // Appcompat
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // Preference
    implementation("androidx.preference:preference:1.2.1")

    // Tests
    androidTestImplementation("androidx.test:runner:1.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.13.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.13.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    androidTestImplementation("androidx.navigation:navigation-testing:$navVersion")

    // Allow use of newer Java Features
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")

}

spotless {
    format("misc") {
        // define the files to apply `misc` to
        target("*.gradle", ".gitattributes", ".gitignore")

        // define the steps to apply to those files
        trimTrailingWhitespace()
        // leadingSpacesToTabs() // or leadingTabsToSpaces. Takes an integer argument if you don"t like 4
        endWithNewline()
    }
    java {
        target("src/*/java/**/*.java")
        // Use the default importOrder configuration
        importOrder()
        removeUnusedImports()
        // apply a specific flavor of palantir-java-format
        leadingSpacesToTabs()
        palantirJavaFormat("2.61.0").formatJavadoc(true)

        // fix formatting of type annotations
        // formatAnnotations()
    }
}
