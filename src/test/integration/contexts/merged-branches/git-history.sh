#!/bin/bash

git --version
rm -rf .git
git init
git config user.name nobody
git config user.email nobody@nowhere.com
dd if=/dev/urandom of=content bs=512 count=2
git add .
git commit --message=initial_commit
git tag -a 1.0.0 --message=release_1.0.0
git checkout -b bugfix
dd if=/dev/urandom of=content bs=512 count=2
git add -u
git commit --message=modif_on_C
git tag -a 1.1.0 --message=release_1.1.0
git checkout master
dd if=/dev/urandom of=content bs=512 count=2
git add -u
git commit --message=modif_on_B
git tag -a 2.0.0 --message=release_2.0.0
git merge --strategy-option theirs --message=merge_bugfix bugfix
git status
git log --graph --oneline --decorate=full
