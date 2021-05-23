# gradle-jgitver-plugin

[![Build Status](https://travis-ci.org/jgitver/gradle-jgitver-plugin.svg?branch=master)](https://travis-ci.org/jgitver/gradle-jgitver-plugin)

gradle plugin to define project version using [jgitver](https://github.com/jgitver/jgitver).

In order to find the latest version published of [gradle-jgitver-plugin](https://github.com/jgitver/gradle-jgitver-plugin), go to the [gradle plugin portal](https://plugins.gradle.org/plugin/fr.brouillard.oss.gradle.jgitver).

## Usage

see the project [build.gradle.kts](build.gradle.kts) to see how the project is using itself to manage its own versions.

### Add the plugin

```
plugins {
  id "fr.brouillard.oss.gradle.jgitver" version "0.9.1"
}
```

### Use it

Besides calculating a version automatically when running any gradle task, like `gradle build`, the plugin registers a
task `version` which you can call to print out the calculated version of your project:

```
$ ./gradlew version

> Task :version
Version: 0.10.1-rc03-2-dirty

BUILD SUCCESSFUL in 1s
1 actionable task: 1 executed
```

## Documentation

See [jgitver](https://github.com/jgitver/jgitver) for a full understanding of the possibilities and usage.

You can also have a look at the maven equivalent: [jgitver-maven-plugin](https://github.com/jgitver/jgitver-maven-plugin).

Finally have a look at the [configuration](#configuration) paragraph to have full control on the plugin.

![Gradle Example](src/doc/images/gradle-example.gif?raw=true "Gradle Example")

### Configuration

#### build.gradle

Optionally, configure the plugin inside the `build.gradle`.

~~~~
jgitver {
  strategy MAVEN | CONFIGURABLE | PATTERN
  mavenLike true/false          (deprecated, use strategy instead)
  policy MAX | LATEST | NEAREST
  autoIncrementPatch true/false
  useDistance true/false
  useDirty true/false
  useSnapshot true/false
  failIfDirty true/false
  useGitCommitTimestamp true/false
  useGitCommitID true/false
  gitCommitIDLength integer
  maxDepth integer
  nonQualifierBranches string   (comma separated list of branches)
  versionPattern string         (only for PATTERN strategy)
  tagVersionPattern string      (only for PATTERN strategy)
  policy {                         repeatable closure
    pattern string              (regexp with capturing group)
    transformations array       (array of string)
  }
  distanceCalculatorKind FIRST_PARENT | LOG | DEPTH 
}

~~~~

#### build.gradle.kts

Optionally, configure the plugin inside the `build.gradle.kts`.

~~~~
jgitver {
  strategy = fr.brouillard.oss.jgitver.Strategies.PATTERN | MAVEN | CONFIGURABLE
  mavenLike = true/false            (deprecated, use strategy instead)
  policy = fr.brouillard.oss.jgitver.LookupPolicy.MAX | LATEST | NEAREST
  autoIncrementPatch = true/false
  useDistance = true/false
  useDirty = true/false
  useSnapshot = true/false
  failIfDirty = true/false
  useGitCommitTimestamp = true/false
  useGitCommitID = true/false
  gitCommitIDLength = integer
  maxDepth = integer
  nonQualifierBranches = "string"   (comma separated list of branches)
  versionPattern = "string"         (only for PATTERN strategy)
  tagVersionPattern = "string"      (only for PATTERN strategy)
  policy {                          repeatable closure
    pattern = "string"              (regexp with capturing group)
    transformations array           (array of string)
  }
  distanceCalculatorKind = fr.brouillard.oss.jgitver.impl.DistanceCalculator.CalculatorKind.FIRST_PARENT | LOG | DEPTH 
}

~~~~

#### Defaults
If you do not provide such a configuration (or fill only partial configuration) the following defaults will be used
- _strategy_: `CONFIGURABLE`
- _mavenLike_: `false`
- _policy_: `MAX`
- _autoIncrementPatch_: `true`
- _useDistance_: `true`
- _useDirty_: `false`
- _useSnapshot_: `false`
- _failIfDirty_: `false`
- _useGitCommitTimestamp_: `false`
- _useGitCommitID_: `false`
- _gitCommitIDLength_: `8`
- _maxDepth_: `Integer.MAX_VALUE`
- _nonQualifierBranches_: `'master'`
- _versionPattern_: no default value
- _tagVersionPattern_: no default value
- _regexVersionTag_: `'Java regexp pattern'`
  - if non set or null then [jgitver](https://github.com/jgitver/jgitver) default applies
  - the pattern must be a regular Java [Pattern](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html) string with one matching group
- distanceCalculatorKind: `FIRST_PARENT`

#### Example to provide specific branch policies

Given the following configuration
```
jgitver {
    policy {
		pattern = 'feature_(.*)' 
		transformations = ['REMOVE_UNEXPECTED_CHARS', 'UPPERCASE']
	}
    policy {
		pattern = '(master)' 
		transformations = ['IGNORE']
	}
}
```

when on branch `feature_login-page`, 3 commits after tag `1.0.0` then version resolution will be `1.0.1-3-LOGINPAGE`

```
$ gradlew version

> Task :version
Version: 1.0.1-3-LOGINPAGE
```

#### Add metadata

Jgitver [Metadatas](https://github.com/jgitver/jgitver/blob/master/src/main/java/fr/brouillard/oss/jgitver/metadata/Metadatas.java#L25) are exposed via gradle extension properties using the Metadata name in lower case.

For example, one could enhance it's jar Manifest with the git commit id using:

```
apply plugin: 'java'
apply plugin: 'fr.brouillard.oss.gradle.jgitver'

jar {
    doFirst {
        manifest {
            manifest.attributes 'X-GIT-SHA1': "$project.ext.git_sha1_full"
        }
    }
}
```

#### Building on detached HEAD

When working on a __detached HEAD__, as often on CI environments behind a SCM webhook, no branch information exists anymore from git.
It is possible to provide externally the branch information via a system property or an environment variable.

- all operating systems/shells: `gradlew version -Djgitver.branch=mybranch`
- bash only (_zsh?_) one line: `JGITVER_BRANCH=mybranch && gradlew version`
- *nix shell: 
    - `export JGITVER_BRANCH=mybranch`
    - `gradlew version`
- windows:
    - `SET JGITVER_BRANCH=mybranch`
    - `gradlew version`

## Local build & sample test project

- `$ ./gradlew install version` will install the current version inside the local maven repository and will print the published version
- minimal test project `build.gradle.kts` file
  ````gradle
  plugins {
    id("fr.brouillard.oss.gradle.jgitver") version "0.9.1"
  }
  repositories {
    mavenLocal()
  }
  ````
- test project `build.gradle.kts` file with Maven like versioning
  ````gradle
  plugins {
    id("fr.brouillard.oss.gradle.jgitver") version "0.9.1"
  }
  repositories {
    mavenLocal()
  }
  
  jgitver {
    strategy = fr.brouillard.oss.jgitver.Strategies.MAVEN
  }
  ````

## Integration tests

Some integration tests are available to make some manual trials/verifications of the plugin.

````
# first install it into the local maven repo, then read the current version into a variable,
# afterwards call it using the following schema:
# bash src/test/integration/test/build.sh CONTEXT JGITVER_GRADLE_VERSION EXPECTED_COMPUTED_VERSION
./gradlew install version
GRADLE_JGIT_VERSION=`gradle version | grep "Version" | awk '{print $2}'`
echo GRADLE_JGIT_VERSION
./src/test/integration/test/build.sh tag-regexp ${GRADLE_JGIT_VERSION} 2.0.1-1
````

## Linux environment for windows users

The easiest way to get started from Windows is to launch a docker container:

- `docker -v run --rm -it -v %CD%:/project -w /project adoptopenjdk/openjdk8 /bin/bash`
- `$ apt-get update && apt-get install -y git`

## Release

- `git tag -a -s -m "release X.Y.Z, additionnal reason" X.Y.Z`: tag the current HEAD with the given tag name. The tag is signed by the author of the release. Adapt with gpg key of maintainer.
    - Matthieu Brouillard command:  `git tag -a -s -u 2AB5F258 -m "release X.Y.Z, additionnal reason" X.Y.Z`
    - Matthieu Brouillard [public key](https://sks-keyservers.net/pks/lookup?op=get&search=0x8139E8632AB5F258)
- `./gradlew publishPlugins`
- `git push --follow-tags origin master`
