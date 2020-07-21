#!/bin/bash

git --version
rm -rf .git
git init
git config user.name nobody
git config user.email nobody@nowhere.com
dd if=/dev/urandom of=content bs=512 count=2
git add .
git commit --message=initial_commit_A
git tag -a --message=tag_1.0.0 1.0.0
dd if=/dev/urandom of=content bs=512 count=2
git add -u
git commit --message=commit_B
git tag -a --message=tag_2.0.0 2.0.0
git status
git log --graph --oneline --decorate=full
