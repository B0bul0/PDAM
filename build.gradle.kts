plugins {
    idea
    java
}

subprojects {
    apply(plugin = "java")

    group = project.property("baseGroup") as String
    version = project.property("version") as String

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(8))
    }

//    base.archivesName.set("pdam-${project.name}")

    repositories {
        mavenCentral()
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
//        options.release.set(8)
    }
}