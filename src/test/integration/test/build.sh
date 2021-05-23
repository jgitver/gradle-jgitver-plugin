#!/bin/bash

# find the scripts directory, cd to it and back after the script run
# this will work if it is not symlinked, see for details here:
# https://stackoverflow.com/questions/59895/how-to-get-the-source-directory-of-a-bash-script-from-within-the-script-itself
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
pushd $DIR
trap "popd" EXIT

if [[ $# -ne 3 ]] ; then
    echo 'Usage: bash build.sh CONTEXT JGITVER_GRADLE_VERSION EXPECTED_COMPUTED_VERSION'
    echo ''
    echo 'Example: bash build.sh merged-branches 0.5.1-2 2.0.1-1'
    echo ''
    exit 1
fi

if [[ -f ../contexts/$1/jgitver.gradle.kts ]] ; then
    if [[ -f ../contexts/$1/git-history.sh ]] ; then
        ../contexts/$1/git-history.sh
        cat base-build.gradle.kts ../contexts/$1/jgitver.gradle.kts > build.gradle.kts
        (export JGITVER_GRADLE_VERSION=$2 && ./gradlew -b build.gradle.kts version) | tee build.log
        COMPUTED_VERSION=`grep Version: build.log | cut -f 2 -d ' '`
        rm build.log
        if [ "$COMPUTED_VERSION" != "$3" ]; then
            echo Error: computed version $COMPUTED_VERSION differs from expected version $3
            exit 2
        fi
    else
        echo Missing build-$1.sh found
    fi
else
    echo Missing build.gradle-$1 found
fi