import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion


plugins {
    kotlin("multiplatform") version "1.9.20-Beta-205"
//    kotlin("multiplatform") version "1.9.10" // no issue with 1.9.10
}

group = "me.username"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

val configuration = project.configurations.create("kotlinPlugin")
val composeSourceOption =
    "plugin:androidx.compose.compiler.plugins.kotlin:sourceInformation=true"


val composeCompiler = when (val kotlinVersion = project.getKotlinPluginVersion()) {
    "1.9.10" -> "1.5.2" // no issue with 1.9.10
    "1.9.20-Beta-205" -> "0.0.0-1.9.20-Beta-205"
    else -> error("??? Unexpected kotlin version $kotlinVersion")
}
project.dependencies.add(configuration.name, "org.jetbrains.compose.compiler:compiler:$composeCompiler")

val kotlinPlugin = configuration.incoming.artifactView {
    attributes {
        attribute(
            Attribute.of("artifactType", String::class.java),
            ArtifactTypeDefinition.JAR_TYPE
        )
    }
}.files

project.tasks.withType(KotlinCompile::class.java).configureEach {
    this.kotlinOptions.freeCompilerArgs += "-Xplugin=${kotlinPlugin.first()}"
    this.kotlinOptions.freeCompilerArgs += listOf("-P", composeSourceOption)
}



kotlin {

//     Tried this too, but the same issue:
//    kotlin.compilerOptions.freeCompilerArgs.add("-Xplugin=${kotlinPlugin.first()}")
//    kotlin.compilerOptions.freeCompilerArgs.addAll(listOf("-P", composeSourceOption))


    val hostOs = System.getProperty("os.name")
    val isArm64 = System.getProperty("os.arch") == "aarch64"
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" && isArm64 -> macosArm64("native")
        hostOs == "Mac OS X" && !isArm64 -> macosX64("native")
        hostOs == "Linux" && isArm64 -> linuxArm64("native")
        hostOs == "Linux" && !isArm64 -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    js(IR) { // no issue for k/js
        nodejs {
            binaries.executable()
        }
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.compose.runtime:runtime:1.5.0")
            }
        }
        val nativeMain by getting
        val nativeTest by getting
    }
}

