plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "self.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.23.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.skyscreamer:jsonassert:1.5.1")
}

tasks.test {
    useJUnitPlatform()
}

javafx {
    version = "17"
    modules("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("self.example.sdui4j.MainApp")
}