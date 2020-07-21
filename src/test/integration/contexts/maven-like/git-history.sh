#!/bin/bash

git --version
rm -rf .git
git init
git config user.name nobody
git config user.email nobody@nowhere.com
dd if=/dev/urandom of=content bs=512 count=2
git add .
git commit --message=initial_commit
git status
git log --graph --oneline --decorate=full
