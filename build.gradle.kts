plugins {
    java
    id("io.quarkus")
    id("org.openapi.generator") version "7.14.0"
}

repositories {
    mavenCentral()
    mavenLocal()
    gradlePluginPortal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project


group = "my-groupId"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

val codegenDir: Directory = layout.buildDirectory.dir("generated-src").get()
sourceSets {
    create("generatedApi") {
        java.srcDir("$codegenDir/src/gen/java")
        resources.srcDir("$codegenDir/src/gen/resources")
    }
}


dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-hibernate-validator")
    implementation("io.quarkus:quarkus-reactive-routes")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")


    implementation(sourceSets["generatedApi"].output)
    testImplementation(sourceSets["generatedApi"].output)
    "generatedApiImplementation"(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    "generatedApiImplementation"("io.quarkus:quarkus-rest")
    "generatedApiImplementation"("io.quarkus:quarkus-rest-jackson")
    "generatedApiImplementation"("io.quarkus:quarkus-hibernate-validator")
    "generatedApiImplementation"("io.quarkus:quarkus-reactive-routes")
}

tasks.named("openApiGenerate") {
    inputs
        .files(fileTree("$projectDir/src/main/resources/openapi") {
            include("**/*.yaml")
        })
        .withPathSensitivity(PathSensitivity.RELATIVE)
    inputs.files(fileTree("$rootDir/openapi-templates/jaxrs-spec") {
        include("**/*.mustache")
    }).withPathSensitivity(PathSensitivity.RELATIVE)
}

tasks.named("compileGeneratedApiJava") {
    dependsOn("openApiGenerate")
}

tasks.named("compileJava") {
    dependsOn("compileGeneratedApiJava")
}

tasks.named("processGeneratedApiResources") {
    dependsOn("openApiGenerate")
}

tasks.named("processGeneratedApiResources") {
    dependsOn("openApiGenerate")
}

openApiGenerate {
    generatorName = "jaxrs-spec"
    inputSpec = "${projectDir}/src/main/resources/openapi/openapi.yaml"
    outputDir = codegenDir.toString()
    apiPackage = "my.groupId.gen.api"
    modelPackage = "my.groupId.gen.dtos"
    templateDir = "${rootDir}/openapi-templates/jaxrs-spec"

    configOptions = mapOf(
        "dateLibrary" to "java8",
        "hideGenerationTimestamp" to "true",
        "interfaceOnly" to "true",
        "openApiNullable" to "false",
        "returnResponse" to "true",
        "useJakartaEe" to "true",
        "useSwaggerAnnotations" to "false",
        "useTags" to "true",
    )
}