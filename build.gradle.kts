import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    kotlin("jvm") version "2.2.21"
    id("org.jetbrains.intellij.platform")
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

kotlin {
    jvmToolchain(21)
}

dependencies {
    intellijPlatform {
        intellijIdea(providers.gradleProperty("platformVersion"))

        jetbrainsRuntime()

        testFramework(TestFrameworkType.Platform)
    }

    implementation("com.google.code.gson:gson:2.13.2")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testImplementation("org.opentest4j:opentest4j:1.3.0")
    testRuntimeOnly("junit:junit:4.13.2")
}

intellijPlatform {

    instrumentCode = false

    pluginConfiguration {

        id = "com.jsonformatter.format-transformer"

        name = providers.gradleProperty("pluginName")

        version = providers.gradleProperty("pluginVersion")

        description = """
            Convert JSON into developer-friendly formats including properties, YAML, XML, CSV, Markdown, and Java code.
        """.trimIndent()

        vendor {
            name = "Abhitabh Kumar"
            email = "abhitabhpersonal2024@gmail.com"
            url = "https://github.com/abhitabh-kumar/Format-Transformer"
        }

        ideaVersion {
            sinceBuild = "261"
        }
    }
}

tasks {

    test {
        useJUnitPlatform()
    }

}
