
plugins {
    `maven-publish`
    java
    signing
}

subprojects {
    version = "2.0.5"
    group = "net.lucypoulton"

    apply<MavenPublishPlugin>()
    apply<SigningPlugin>()
    apply<JavaPlugin>()

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }
    }

    repositories {
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        mavenCentral()
    }

    dependencies {
        implementation("net.lucypoulton:squirtgun-api:2.0.0-pre9")
        compileOnly ("me.clip:placeholderapi:2.11.6")
    }
}