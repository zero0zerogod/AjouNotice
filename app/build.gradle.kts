import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.scheschedule"
    compileSdk = 35

    val localPropertiesFile = rootProject.file("local.properties")
    var apiBaseUrl: String = "http://10.0.2.2:8080/" // 기본값

    // local.properties에서 API_BASE_URL 읽기
    if (localPropertiesFile.exists()) {
        val properties = Properties()
        properties.load(localPropertiesFile.inputStream())
        apiBaseUrl = properties.getProperty("API_BASE_URL", apiBaseUrl)
    }

    // 시스템 환경변수 API_BASE_URL 우선 적용 (릴리스 빌드 시 사용)
    if (project.hasProperty("API_BASE_URL")) {
        apiBaseUrl = project.property("API_BASE_URL") as String
    }

    defaultConfig {
        applicationId = "com.example.scheschedule"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrl\"")

    }

    buildFeatures {
        buildConfig = true // BuildConfig 기능 활성화
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrl\"") // local.properties에서 로드된 URL 사용
        }

        release {
            // 환경변수 기반으로 설정된 API_BASE_URL 사용
            buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrl\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Navigation Compose 추가
    implementation(libs.androidx.navigation.compose) // 네비게이션 지원

    // Material Icons 추가 (선택 사항)
    implementation(libs.androidx.material.icons.core) // 기본 아이콘
    implementation(libs.androidx.material.icons.extended) // 확장된 아이콘

    // Retrofit 라이브러리
    implementation(libs.retrofit) // Retrofit 기본 라이브러리

    // Gson 변환기 (JSON 파싱)
    implementation(libs.converter.gson)

    // Coroutine 지원을 위한 Retrofit 어댑터 (선택 사항)
    implementation(libs.retrofit2.kotlin.coroutines.adapter)

    // 테스트용 라이브러리 (선택 사항)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // OkHttp Logging Interceptor 추가
    implementation(libs.logging.interceptor)
}