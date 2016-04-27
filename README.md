# gradle-jgitver-plugin

gradle plugin to define project version using [jgitver](https://github.com/McFoggy/jgitver).

Starting from version _0.0.1_ [gradle-jgitver-plugin](https://github.com/jgitver/gradle-jgitver-plugin) is now published to the [gradle plugin portal](https://plugins.gradle.org/plugin/fr.brouillard.oss.gradle.jgitver).

## Usage

see the project [build.gradle](build.gradle) to see how the project is using itself to manage its own versions.

```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.fr.brouillard.oss.gradle:gradle-jgitver-plugin:0.0.1"
  }
}

apply plugin: 'fr.brouillard.oss.gradle.jgitver'
```

In the current version, no configuration is possible.
The plugin uses [jgitver](https://github.com/McFoggy/jgitver) with the following settings:

- _autoIncrementPatch_: `true`
- _nonQualifierBranches_: `master`
- _useDistance_: `true`
- _useGitCommitId_: `false`

## Documentation

See [jgitver](https://github.com/McFoggy/jgitver) & [maven-external-version-jgitver](https://github.com/McFoggy/maven-external-version-jgitver) to better understand what it does

![Example](src/doc/images/s1_linear_with_only_annotated_tags_autoIncrement.gif?raw=true "Example")

## Release to gradle plugin portal

`./gradlew publishPlugins`