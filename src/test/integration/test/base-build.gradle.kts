plugins {
  id("fr.brouillard.oss.gradle.jgitver") version "0.9.1"
}

repositories {
  mavenLocal()
  maven {
    url = uri("https://plugins.gradle.org/m2/")
  }
}
