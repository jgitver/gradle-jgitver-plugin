plugins {
    id("com.gradle.plugin-publish") version "0.12.0"
    id("fr.brouillard.oss.gradle.jgitver") version "0.9.1"
    id("java-gradle-plugin")
    id("maven")       // for local tests
    id("se.bjurr.gitchangelog.git-changelog-gradle-plugin") version "1.64"
}

group = "fr.brouillard.oss.gradle"

jgitver {
    useDirty = true
}

repositories {
    mavenCentral()
    jcenter()
    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

gradlePlugin {
    plugins {
        create("jgitverPlugin") {
            displayName = "jgitver Gradle plugin"
            id = "fr.brouillard.oss.gradle.jgitver"
            implementationClass = "fr.brouillard.oss.gradle.plugins.JGitverPlugin"
        }
    }
}

pluginBundle {
  website = "https://github.com/jgitver/gradle-jgitver-plugin"
  vcsUrl = "https://github.com/jgitver/gradle-jgitver-plugin"
  description = "gradle plugin that defines automatically project version using jgitver"
  tags = listOf("versioning", "jgitver", "git")
}

dependencies {
    implementation("fr.brouillard.oss:jgitver:0.12.0")
}

tasks.register("changelog", se.bjurr.gitchangelog.plugin.gradle.GitChangelogTask::class) {
    file("CHANGELOG.md")
}

tasks.withType<JavaCompile> {
    options.isDeprecation = true
}
