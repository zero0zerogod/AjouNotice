import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // 구글 서비스 플러그인 적용
    id("com.google.gms.google-services")
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
            // 에뮬레이터 -> 로컬 서버
            buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:8080/\"") // 로컬 서버
        }

        release {
            // 1) .env 파일 로드
            val envFile = rootProject.file(".env")
            val props = Properties()

            if (envFile.exists()) {
                props.load(envFile.inputStream())
            }

            // 2) .env 에서 실제 서버 주소 가져오기
            val releaseServerUrl = props.getProperty("RELEASE_SERVER_URL", "https://default-server.com/")

            // 3) release 빌드에 실제 서버 주소 주입
            buildConfigField("String", "API_BASE_URL", "\"$releaseServerUrl\"")

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
    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.androidx.compose.bom.v20250100))
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.androidx.storage)
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