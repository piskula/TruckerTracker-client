plugins {
    alias(libs.plugins.kotlin.spring) // all-open for @RestController, @Service, etc.
    alias(libs.plugins.kotlin.jpa)    // no-arg constructors for @Entity classes
    alias(libs.plugins.spring.boot)   // bootJar + bootRun tasks
}

dependencies {
    implementation(project(":module-api"))

    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.oauth2.resource.server)
    implementation(libs.springdoc.openapi.webmvc.ui)  // Swagger UI + API docs
    implementation(libs.jackson.module.kotlin)         // Kotlin data class serialization

    implementation(libs.spring.boot.starter.liquibase)
    implementation(libs.minio)

    runtimeOnly(libs.postgresql)

    testImplementation(libs.spring.boot.starter.test)
}
