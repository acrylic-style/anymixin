import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

val asmVersion = "9.4"

dependencies {
    api("com.mojang:logging:1.0.0")
    api("org.slf4j:slf4j-api:2.0.5")
    api(project(":"))
}

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

        transform(Log4j2PluginsCacheFileTransformer::class.java)
        archiveFileName.set("anymixin.jar")
    }
}
