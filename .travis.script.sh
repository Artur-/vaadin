#!/usr/bin/env bash

# TRAVIS_PULL_REQUEST == "false" for a normal branch commit, the PR number for a PR
# TRAVIS_BRANCH == target of normal commit or target of PR
# TRAVIS_SECURE_ENV_VARS == true if encrypted variables are available
# TRAVIS_REPO_SLUG == the repository, e.g. vaadin/vaadin

mvn -B -e -V clean verify
# Should really run javadoc:javadoc also, but it fails miserably right now

if [ "$TRAVIS_PULL_REQUEST" != "false" ] && [ "$TRAVIS_SECURE_ENV_VARS" == "true" ]
then
	# Pull request with secure vars available
	ant -lib ~/.m2/repository/org/apache/ivy/ivy/2.4.0/ -f uitest/build.xml test-tb3
fi
