#!/bin/bash

git --version
rm -rf .git
git init
git config user.name nobody
git config user.email nobody@nowhere.com
dd if=/dev/urandom of=content bs=512 count=2
git add .
git commit --message=initial_commit
git tag -a -m "v1.1.0" v1.1.0
dd if=/dev/urandom of=content bs=512 count=2
git add .
git commit --message=commit_1_2_0
git tag -a -m "v1.2.0" v1.2.0
dd if=/dev/urandom of=content bs=512 count=2
git add .
git commit --message=commit_1_2_1
git tag -a -m "v1.2.1" v1.2.1
dd if=/dev/urandom of=content bs=512 count=2
git add .
git commit --message=commit_1_2_2
git tag -a -m "v1.2.2" v1.2.2
dd if=/dev/urandom of=content bs=512 count=2
git add .
git commit --message=third_commit
git tag -a -m "v1.3.0" v1.3.0
git status
git log --graph --oneline
