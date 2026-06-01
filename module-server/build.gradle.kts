plugins {
    alias(libs.plugins.kotlin.spring) // all-open for @RestController, @Service, etc.
    alias(libs.plugins.spring.boot)   // bootJar + bootRun tasks
}

dependencies {
    implementation(project(":module-api"))

    implementation(libs.spring.boot.starter.web)
    implementation(libs.springdoc.openapi.webmvc.ui)  // Swagger UI + API docs
    implementation(libs.jackson.module.kotlin)         // Kotlin data class serialization

    testImplementation(libs.spring.boot.starter.test)
}
