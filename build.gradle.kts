plugins {
    id("com.gradle.plugin-publish") version "0.12.0"
    id("fr.brouillard.oss.gradle.jgitver") version "0.9.1"
    id("java-gradle-plugin")
    id("se.bjurr.gitchangelog.git-changelog-gradle-plugin") version "1.64"
}

group = "fr.brouillard.oss.gradle"

jgitver {
    useDirty = true
    useDistance = false
}

repositories {
    mavenCentral()
    jcenter()
    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

pluginBundle {
  website = "https://github.com/jgitver/gradle-jgitver-plugin"
  vcsUrl = "https://github.com/jgitver/gradle-jgitver-plugin"
  tags = listOf("versioning", "jgitver", "git")
}

gradlePlugin {
    plugins {
        create("jgitverPlugin") {
            id = "fr.brouillard.oss.gradle.jgitver"
            displayName = "jgitver Gradle plugin"
            description = "gradle plugin that defines automatically project version using jgitver"
            implementationClass = "fr.brouillard.oss.gradle.plugins.JGitverPlugin"
        }
    }
}

dependencies {
    implementation(gradleApi())
    implementation("fr.brouillard.oss:jgitver:0.12.0")
}

tasks.register("changelog", se.bjurr.gitchangelog.plugin.gradle.GitChangelogTask::class) {
    file("CHANGELOG.md")
}

tasks.withType<JavaCompile> {
    options.isDeprecation = true
}
