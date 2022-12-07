plugins {
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
}

dependencies {
    compileOnly("org.spongepowered:mixin:0.8.5")
    compileOnly("org.spigotmc:spigot:1.19.2-R0.1-SNAPSHOT")
}

tasks {
    jar {
        manifest {
            attributes(
                mapOf(
                    "TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
                    "MixinConfigs" to "mixins.example.json",
                )
            )
        }
    }
}
