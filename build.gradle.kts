plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "xyz.acrylicstyle.anymixin"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
    maven { url = uri("https://repo.blueberrymc.net/repository/maven-public/") }
}

dependencies {
    implementation("org.spongepowered:mixin:0.8.5")
    implementation("net.blueberrymc:native-util:2.1.0")
    implementation("org.ow2.asm:asm:9.4")
    implementation("org.ow2.asm:asm-util:9.4")
    implementation("org.ow2.asm:asm-tree:9.4")
    implementation("org.ow2.asm:asm-analysis:9.4")
    implementation("org.ow2.asm:asm-commons:9.4")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.google.code.gson:gson:2.10")
    implementation("net.minecraft:launchwrapper:1.12") {
        exclude("org.lwjgl.lwjgl", "lwjgl")
        exclude("org.lwjgl", "lwjgl")
        exclude("org.lwjgl", "lwjgl-openal")
        exclude("org.apache.logging.log4j", "log4j-api")
        exclude("org.apache.logging.log4j", "log4j-core")
        exclude("org.ow2.asm", "asm-debug-all")
    }
    implementation("org.apache.logging.log4j:log4j-api:2.19.0")
    implementation("org.apache.logging.log4j:log4j-core:2.19.0")
    compileOnly("org.jetbrains:annotations:23.0.0")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(8))

tasks {
    shadowJar {
        manifest {
            attributes(
                mapOf(
                    "Main-Class" to "xyz.acrylicstyle.anymixin.AnyMixin",
                    "Launcher-Agent-Class" to "xyz.acrylicstyle.anymixin.AnyMixin",
                    "Implementation-Version" to project.version,
                    "Can-Retransform-Classes" to "true",
                    "Can-Redefine-Classes" to "true",
                    "Multi-Release" to "true",
                )
            )
        }

        archiveFileName.set("anymixin.jar")
    }
}
