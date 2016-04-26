# gradle-jgitver-plugin

gradle plugin to define project version using [jgitver](https://github.com/McFoggy/jgitver)

## Usage

add to you gradle.build `apply plugin: 'fr.brouillard.oss.gradle.jgitver'`

In this version, no configuration is possible.
The plugin uses [jgitver](https://github.com/McFoggy/jgitver) with:

- _autoIncrementPatch_: `true`
- _nonQualifierBranches_: `master`
- _useDistance_: `true`
- _useGitCommitId_: `false`

## Documentation

See [jgitver](https://github.com/McFoggy/jgitver) & [maven-external-version-jgitver](https://github.com/McFoggy/maven-external-version-jgitver) to understand what it does