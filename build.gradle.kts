plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
    `java-library`
}

allprojects {
    apply {
        plugin("java")
        plugin("com.github.johnrengelman.shadow")
        plugin("java-library")
    }

    group = "xyz.acrylicstyle.anymixin"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
        maven { url = uri("https://repo.blueberrymc.net/repository/maven-public/") }
    }
}

val asmVersion = "9.4"

dependencies {
    api("org.spongepowered:mixin:0.8.5")
    api("net.blueberrymc:native-util:2.1.0")
    api("org.ow2.asm:asm:$asmVersion")
    api("org.ow2.asm:asm-util:$asmVersion")
    api("org.ow2.asm:asm-tree:$asmVersion")
    api("org.ow2.asm:asm-analysis:$asmVersion")
    api("org.ow2.asm:asm-commons:$asmVersion")
    api("com.google.guava:guava:31.1-jre")
    api("com.google.code.gson:gson:2.10")
    api("net.minecraft:launchwrapper:1.12") {
        exclude("org.lwjgl.lwjgl", "lwjgl")
        exclude("org.lwjgl", "lwjgl")
        exclude("org.lwjgl", "lwjgl-openal")
        exclude("org.apache.logging.log4j", "log4j-api")
        exclude("org.apache.logging.log4j", "log4j-core")
        exclude("org.ow2.asm", "asm-debug-all")
    }
    api("org.apache.logging.log4j:log4j-api:2.19.0")
    api("org.apache.logging.log4j:log4j-core:2.19.0")
    api("com.lmax:disruptor:3.3.7")
    compileOnlyApi("org.jetbrains:annotations:23.0.0")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(8))

tasks {
    shadowJar {
        manifest {
            attributes(
                mapOf(
                    "Main-Class" to "xyz.acrylicstyle.anymixin.AnyMixin",
                    "Launcher-Agent-Class" to "xyz.acrylicstyle.anymixin.AnyMixin",
                    "Implementation-Version" to asmVersion, // this needs to be set to ASM version
                    "Can-Retransform-Classes" to "true",
                    "Can-Redefine-Classes" to "true",
                    "Multi-Release" to "true",
                )
            )
        }

        archiveFileName.set("anymixin.jar")
    }
}
