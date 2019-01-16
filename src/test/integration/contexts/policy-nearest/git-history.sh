#!/bin/bash
git --version
rm -rf .git
git init
git config user.name nobody
git config user.email nobody@nowhere.com
dd if=/dev/urandom of=content bs=512 count=2
git add .
git commit --message=A_initial_commit_master
git tag -a 2.0.0 --message=release_2.0.0
git tag t0
dd if=/dev/urandom of=content bs=512 count=2
git add -u
git commit --message=B_master
dd if=/dev/urandom of=content bs=512 count=2
git add -u
git commit --message=C_master
git checkout -b bugfix 2.0.0
dd if=/dev/urandom of=content bs=512 count=2
git add -u
git commit --message=D_branch_bugfix
git tag t2
dd if=/dev/urandom of=content bs=512 count=2
git add -u
git commit --message=E_branch_bugfix
git tag t1
git tag -a 1.0.0 --message=release_1.0.0
sleep 2
git tag -a 1.1.0 --message=release_1.1.0 t2
git checkout master
git merge --strategy-option theirs --message=merge_bugfix bugfix
git status
git log --graph --oneline --decorate=full