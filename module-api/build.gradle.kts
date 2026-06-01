// Pure contract module — no Spring Boot plugin, no fat jar.
// Consumers (module-server) provide spring-web and springdoc at runtime.
dependencies {
    compileOnly(libs.springdoc.openapi.webmvc.api)   // @Tag, @Operation, etc.
    compileOnly("org.springframework:spring-web")     // @GetMapping, @PathVariable, etc. — version from BOM
}
