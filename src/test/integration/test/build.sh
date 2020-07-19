#!/bin/bash

if [[ $# -ne 3 ]] ; then
    echo 'Usage: build CONTEXT JGITVER_GRADLE_VERSION EXPECTED_COMPUTED_VERSION'
    echo ''
    echo 'Example: ./build.sh merged-branches 0.5.1-2 2.0.1-1'
    echo ''
    exit 1
fi

if [[ -f ../contexts/$1/jgitver.gradle ]] ; then
    if [[ -f ../contexts/$1/git-history.sh ]] ; then
        bash ../contexts/$1/git-history.sh
        cat base-build.gradle ../contexts/$1/jgitver.gradle > build.gradle
        (export JGITVER_GRADLE_VERSION=$2 && bash gradlew -b build.gradle version) | tee build.log
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
