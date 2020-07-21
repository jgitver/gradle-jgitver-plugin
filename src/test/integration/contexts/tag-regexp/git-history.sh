#!/bin/bash

git --version
rm -rf .git
git init
git config user.name nobody
git config user.email nobody@nowhere.com
dd if=/dev/urandom of=content bs=512 count=2
git add .
git commit --message=initial_commit
git tag -a -m "1.0" 1.0
dd if=/dev/urandom of=content bs=512 count=2
git add .
git commit --message=second_commit
git tag -a -m "r2.0" r2.0
dd if=/dev/urandom of=content bs=512 count=2
git add .
git commit --message=third_commit
git tag -a -m "v3.0" v3.0
git status
git log --graph --oneline --decorate=full
